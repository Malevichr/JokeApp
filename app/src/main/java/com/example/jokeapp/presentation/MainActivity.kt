package com.example.jokeapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.jokeapp.JokeApp
import com.example.jokeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = (application as JokeApp).viewModel

        binding.showFavoriteCheckBox.setOnCheckedChangeListener{_, isChecked ->
            viewModel.chooseFavorites(isChecked)

        }
        binding.favoriteButton.setOnClickListener{
            viewModel.changeJokeStatus()
        }

        binding.button.setOnClickListener {
            binding.button.isEnabled = false
            binding.progressBar.visibility = View.VISIBLE
            viewModel.getJoke()
        }
        viewModel.init(object: JokeUiCallback {
            override fun provideText(text: String) = runOnUiThread{
                binding.button.isEnabled = true
                binding.progressBar.visibility = View.INVISIBLE
                binding.textView.text = text
            }

            override fun provideIconResId(iconResId: Int) = runOnUiThread {
                binding.favoriteButton.setImageResource(iconResId)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clear()
    }
}