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

    @Before
    fun setUp() {
        repository = FakeRepository()
        dispatcherList = FakeDispatcherList()
        toFavoriteMapper = FakeMapper(true)
        toBaseMapper = FakeMapper(false)
        jokeUiCallback = FakeJokeUiCallback()

        viewModel = MainViewModel(repository, toFavorite = toFavoriteMapper, toBaseUi = toBaseMapper, dispatcherList = dispatcherList)
        viewModel.init(jokeUiCallback)
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
        val expectedText = "Joke"
        val expectedIconId = 0
        assertEquals(expectedText, jokeUiCallback.provideTextList[0])
        assertEquals(expectedIconId, jokeUiCallback.provideIconResIdList[0])



        assertEquals(1, jokeUiCallback.provideTextList.size)

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

        val expectedText = "Joke"
        val expectedIconId = 1

        assertEquals(expectedText, jokeUiCallback.provideTextList[0])
        assertEquals(expectedIconId, jokeUiCallback.provideIconResIdList[0])

        assertEquals(1, jokeUiCallback.provideTextList.size)
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

        assertEquals(expectedText, jokeUiCallback.provideTextList[0])
        assertEquals(expectedIconId, jokeUiCallback.provideIconResIdList[0])

        assertEquals(1, jokeUiCallback.provideTextList.size)
    }
    @Test
    fun test_change_joke_status(){
        repository.returnChaneJokeStatus = FakeJokeResult(FakeJoke("id", "Joke"), true, true, "error")
        viewModel.changeJokeStatus()

        val expectedText = "Joke"
        val expectedIconId = 0

        assertEquals(expectedText, jokeUiCallback.provideTextList[0])
        assertEquals(expectedIconId, jokeUiCallback.provideIconResIdList[0])

        assertEquals(1, jokeUiCallback.provideTextList.size)
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

    override suspend fun map(id: String, text: String): JokeUi {
        return FakeJokeUi(text, id, isFavorite)
    }
}

private data class FakeJokeUi(val text: String, val id: String, val isFavorite: Boolean) :
    JokeUi(text, if(isFavorite) 1 else 0)

private data class FakeJoke(
    private val id: String,
    private val text: String
) : Joke {
    override suspend fun <T> map(mapper: Joke.Mapper<T>): T {
        return mapper.map(id, text)
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
    override suspend fun fetch(): JokeResult {
        return returnFetchJokeResult!!
    }
    var returnChaneJokeStatus: FakeJokeResult? = null
    override suspend fun changeJokeStatus(): JokeResult {
        returnChaneJokeStatus!!.isFavorites = !(returnChaneJokeStatus!!.isFavorites)
        return returnChaneJokeStatus!!
    }

    override fun chooseFavorites(favorite: Boolean) {
        TODO("Not yet implemented")
    }

}