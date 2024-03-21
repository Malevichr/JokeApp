package com.example.jokeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.jokeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = (application as JokeApp).viewModel

        binding.button.setOnClickListener {
            binding.button.isEnabled = false
            binding.progressBar.visibility = View.VISIBLE
            viewModel.getJoke(binding.languageView.text.toString())
        }
        viewModel.init(object: TextCallback{
            override fun provideText(text: String) = runOnUiThread{
                binding.button.isEnabled = true
                binding.progressBar.visibility = View.INVISIBLE
                binding.textView.text = text
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clear()
    }
}