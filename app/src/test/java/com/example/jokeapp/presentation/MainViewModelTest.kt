package com.example.jokeapp.presentation

import com.example.jokeapp.data.Error
import com.example.jokeapp.data.Joke
import com.example.jokeapp.data.JokeResult
import com.example.jokeapp.data.Repository
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Test


class MainViewModelTest {
    private lateinit var repository: FakeRepository
    private lateinit var viewModel: MainViewModel
    private lateinit var toFavoriteMapper: Joke.Mapper<JokeUi>
    private lateinit var toBaseMapper: Joke.Mapper<JokeUi>
    private lateinit var jokeUiCallback: FakeJokeUiCallback
    private lateinit var dispatcherList: DispatcherList
    private lateinit var communication: FakeCommunication

    @Before
    fun setUp() {
        repository = FakeRepository()
        dispatcherList = FakeDispatcherList()
        toFavoriteMapper = FakeMapper(true)
        toBaseMapper = FakeMapper(false)
        jokeUiCallback = FakeJokeUiCallback()
        communication = FakeCommunication()

        viewModel = MainViewModel(repository, toFavoriteMapper, toBaseMapper, communication, dispatcherList )
    }

    @Test
    fun test_successful_not_favorite() {
        repository.returnFetchJokeResult = FakeJokeResult(
            FakeJoke("1", "Joke"),
            false,
            true,
            "error"
        )
        viewModel.getJoke()

        val expectedJoke = FakeJokeUi("Joke", id="1", false)

        assertEquals(expectedJoke, communication.data)

    }
    @Test
    fun test_successful_favorite() {
        repository.returnFetchJokeResult = FakeJokeResult(
            FakeJoke("1", "Joke"),
            true,
            true,
            "error"
        )
        viewModel.getJoke()

        val expectedJoke = FakeJokeUi("Joke", id="1", true)

        assertEquals(expectedJoke, communication.data)
    }
    @Test
    fun test_not_successful() {
        repository.returnFetchJokeResult = FakeJokeResult(
            FakeJoke("1", "Joke"),
            true,
            false,
            "error"
        )
        viewModel.getJoke()

        val expectedText = "error"
        val expectedIconId = 0

        val expectedJoke = JokeUi.Failed("error")

        assertEquals(expectedJoke, communication.data)
    }
    @Test
    fun test_change_joke_status(){
        repository.returnChaneJokeStatus = FakeJokeResult(FakeJoke("id", "Joke"), true, true, "error")
        viewModel.changeJokeStatus()

        val expectedText = "Joke"
        val expectedIconId = 0

        val expectedJoke = FakeJokeUi("Joke", id="id", false)

        assertEquals(expectedJoke, communication.data)
    }
}
private class FakeDispatcherList: DispatcherList{
    private val dispatcher =  TestCoroutineDispatcher()
    override fun io(): CoroutineDispatcher = dispatcher


    override fun ui(): CoroutineDispatcher = dispatcher


}
private class FakeJokeUiCallback: JokeUiCallback{
    public val provideTextList = mutableListOf<String>()
    override fun provideText(text: String) {
        provideTextList.add(text)
    }
    val provideIconResIdList = mutableListOf<Int>()
    override fun provideIconResId(iconResId: Int) {
        provideIconResIdList.add(iconResId)
    }

}
private class FakeMapper(private val isFavorite: Boolean) : Joke.Mapper<JokeUi> {

    override suspend fun map(id: String, text: String, language: String): JokeUi {
        return FakeJokeUi(text, id, isFavorite)
    }
}

private data class FakeJokeUi(val text: String, val id: String, val isFavorite: Boolean) :
    JokeUi.Abstract(text, if(isFavorite) 1 else 0)

private data class FakeJoke(
    private val id: String,
    private val text: String
) : Joke {
    override suspend fun <T> map(mapper: Joke.Mapper<T>): T {
        return mapper.map(id, text, "")
    }

}

private data class FakeJokeResult(
    private val joke: Joke,
    var isFavorites: Boolean,
    private val isSuccessful: Boolean,
    private val error: String
) : JokeResult {
    override fun isFavorite(): Boolean = isFavorites

    override fun isSuccessful(): Boolean = isSuccessful

    override fun errorMessage(): String = error

    override suspend fun <T> map(mapper: Joke.Mapper<T>): T = joke.map(mapper)

}

private class FakeRepository : Repository<JokeResult, Error> {
    var returnFetchJokeResult: JokeResult? = null
    override suspend fun fetch(language: String): JokeResult {
        return returnFetchJokeResult!!
    }
    var returnChaneJokeStatus: FakeJokeResult? = null
    override suspend fun changeJokeStatus(): JokeResult {
        returnChaneJokeStatus!!.isFavorites = !(returnChaneJokeStatus!!.isFavorites)
        return returnChaneJokeStatus!!
    }

    override suspend fun changeLanguage(): JokeResult {
        TODO("Not yet implemented")
    }

    override fun chooseFavorites(favorite: Boolean) {
        TODO("Not yet implemented")
    }

}

private class FakeCommunication: JokeCommunication{
    var data: JokeUi? = null
    override fun map(data: JokeUi) {
        this.data = data
    }
}