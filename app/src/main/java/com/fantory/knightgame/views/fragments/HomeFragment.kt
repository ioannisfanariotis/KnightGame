package com.fantory.knightgame.views.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fantory.knightgame.R
import com.fantory.knightgame.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            moves.setText("3")

            startButton.setOnClickListener {
                if(size.text.toString().isEmpty())
                    Toast.makeText(requireActivity(), getString(R.string.null_size), Toast.LENGTH_SHORT).show()
                else if(moves.text.toString().isEmpty())
                    Toast.makeText(requireActivity(), getString(R.string.null_moves), Toast.LENGTH_SHORT).show()
                else if (size.text.toString().trim().toInt() !in 16 downTo 6)
                    Toast.makeText(requireActivity(), getString(R.string.size_rules), Toast.LENGTH_SHORT).show()
                else {
                    Toast.makeText(requireActivity(), getString(R.string.lets_play), Toast.LENGTH_SHORT).show()
                    val action = HomeFragmentDirections.actionHomeFragmentToGameFragment(getString(R.string.instructions),
                        size.text.toString().trim().toInt(), moves.text.toString().trim().toInt())
                    findNavController().navigate(action)
                }
            }
        }
    }
}