package com.example.jokeapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.jokeapp.JokeApp
import com.example.jokeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = (application as JokeApp).viewModel

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
            override fun provideText(text: String)  {
                binding.button.isEnabled = true
                binding.progressBar.visibility = View.INVISIBLE
                binding.textView.text = text
            }

            override fun provideIconResId(iconResId: Int) {
                binding.favoriteButton.setImageResource(iconResId)
            }
        })
    }
}