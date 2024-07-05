package com.fantory.knightgame.views.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fantory.knightgame.R
import com.fantory.knightgame.databinding.FragmentGameBinding
import com.fantory.knightgame.models.Solution
import com.fantory.knightgame.viewmodels.AppViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameFragment : Fragment() {
    private lateinit var binding: FragmentGameBinding
    private val viewModel: AppViewModel by viewModels()
    private var startPosition: Pair<Int, Int>? = null       //χωρίς συντεταγμένες (ακόμα)
    private var endPosition: Pair<Int, Int>? = null         //χωρίς συντεταγμένες (ακόμα)
    private val allPossibleKnightMoves = arrayOf(
        Pair(2, 1), Pair(2, -1), Pair(-2, 1), Pair(-2, -1),
        Pair(1, 2), Pair(1, -2), Pair(-1, 2), Pair(-1, -2)
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cells = Array(viewModel.size) { arrayOfNulls<TextView>(viewModel.size) }

        binding.apply {

            for (row in 0 until viewModel.size) {
                for (col in 0 until viewModel.size) {
                    //δημιουργία σκακιέρας
                    val cell = TextView(requireActivity()).apply {
                        setBackgroundColor(if ((row + col) % 2 == 0) Color.BLACK else Color.WHITE)
                        layoutParams = GridLayout.LayoutParams().apply {
                            width = 0
                            height = 0
                            columnSpec = GridLayout.spec(col, 1f)
                            rowSpec = GridLayout.spec(row, 1f)
                        }
                        textSize = 18f
                        setTextColor(Color.RED)
                        setTypeface(null, Typeface.BOLD)
                        gravity = android.view.Gravity.CENTER
                    }

                    //τι κάνει κάθε κελί αν πατηθεί
                    cell.setOnClickListener {
                        if (startPosition == null) {
                            startPosition = Pair(row, col)
                            cells[row][col]?.text = "S"
                        } else if (endPosition == null) {
                            endPosition = Pair(row, col)
                            cells[row][col]?.text = "E"

                            val currentPath = mutableListOf(startPosition)                  //ξεκινάει από την αρχή και του προστίθονται κινήσεις
                            val allSolutions = mutableListOf<List<Pair<Int, Int>>>()           //λίστα με όλες τις πιθανές λύσεις

                            fun findSolutions(                                     //αναδρομική συνάρτηση
                                currentMove: Pair<Int, Int>,
                                endPosition: Pair<Int, Int>,
                                movesLeft: Int,
                                currentPath: MutableList<Pair<Int, Int>?>,
                                allSolutions: MutableList<List<Pair<Int, Int>>>
                            ) {
                                if (currentMove == endPosition) {                       //βρέθηκε μια λύση (διαδρομή)
                                    allSolutions.add(currentPath.toList() as List<Pair<Int, Int>>)
                                    return
                                }

                                if (movesLeft == 0) {                                   //τέλος αναδρομής
                                    return
                                }

                                for (move in allPossibleKnightMoves) {                  //δοκιμές όλων των δυνατών κινήσεων
                                    val nextMove = Pair(currentMove.first + move.first, currentMove.second + move.second)       //δημιουργία επόμενης κίνησης
                                    if (nextMove.first in 0..<viewModel.size                //έλεγχος κίνησης για όρια της σκακιέρας
                                        && nextMove.second in 0..<viewModel.size) {
                                        currentPath.add(nextMove)
                                        findSolutions(nextMove, endPosition, movesLeft - 1, currentPath, allSolutions)       //επανάληψη
                                        currentPath.removeAt(currentPath.size - 1)         //αφαίρεση τελευταίας κίνησης για να τη εξερεύνηση υπόλοιπων διαδρομών
                                    }
                                }
                            }

                            findSolutions(startPosition!!, endPosition!!, viewModel.moves, currentPath, allSolutions)

                            //Παρουσίαση διαδρομών
                            allSolutions.forEachIndexed { index, eachPath ->
                                eachPath.forEach { (row, col) ->
                                    cells[row][col]?.apply {
                                        text = (text.toString() + " " + (index + 1))        //κάθε διαδρομή έχει από έναν αριθμό (id)
                                        setBackgroundColor(Color.GRAY)
                                    }
                                }
                            }

                            if (allSolutions.isEmpty())
                                Toast.makeText(requireActivity(), getString(R.string.no_solution_found), Toast.LENGTH_SHORT).show()
                            else
                                viewModel.insertSolution(Solution(0, allSolutions.toString()))
                        }
                    }

                    cells[row][col] = cell
                    chessboard.addView(cell)
                }
            }

            resetButton.setOnClickListener {                    //all reset
                for (row in 0 until viewModel.size) {
                    for (col in 0 until viewModel.size) {
                        cells[row][col]?.apply {
                            text = ""
                            setBackgroundColor(if ((row + col) % 2 == 0) Color.BLACK else Color.WHITE)
                        }
                    }
                }
                startPosition = null
                endPosition = null
            }

            viewModel.apply {
                //showSolution()

                solutionLd.observe(viewLifecycleOwner) {
//                    for (position in it.allPaths) {
//                        val (row, col) = position
//                            cells[row][col]?.apply {
//                                text = ("Solution")
//                                setBackgroundColor(Color.GRAY)
//                        }
//                    }
                }

                validInsertLd.observe(viewLifecycleOwner) {
                    if (it)
                        Toast.makeText(requireActivity(), getString(R.string.success), Toast.LENGTH_SHORT).show()
                }

                loader.observe(viewLifecycleOwner) {
                    if (it)
                        mProgressLarge.visibility = View.VISIBLE
                    else
                        mProgressLarge.visibility = View.GONE
                }

                exceptionErrorDialogLd.observe(viewLifecycleOwner) {
                    Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.dispose()
    }
}