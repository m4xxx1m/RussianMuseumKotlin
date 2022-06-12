package ru.raptors.russian_museum.fragments.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import ru.raptors.russian_museum.R
import ru.raptors.russian_museum.databinding.FragmentGamesBinding

class GamesFragment : Fragment() {
    private var _binding: FragmentGamesBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGamesBinding.inflate(inflater, container, false)
        binding.toFindObject.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id
                .action_navigation_dashboard_to_findObjectActivity)
        }
        binding.toPuzzles.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id
                .action_navigation_games_to_puzzlesActivity)
        }
        binding.toGuessGenre.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id
                .action_navigation_games_to_guessGenreChooseActivity)
        }
        binding.toTests.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id
                .action_navigation_games_to_testSelectionActivity)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}