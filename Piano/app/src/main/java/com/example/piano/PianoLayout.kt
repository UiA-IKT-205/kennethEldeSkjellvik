package com.example.piano

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.piano.databinding.FragmentPianoBinding
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_piano.view.*


class PianoLayout : Fragment() {
    private var _binding:FragmentPianoBinding? = null
    private val binding get() = _binding!!

    private val fullToneKeys = listOf("C", "D", "E", "F", "G", "A", "B", "CC", "DD", "EE", "FF", "GG", "AA", "BB", "CCC", "DDD", "EEE", "FFF", "GGG", "AAA", "BBB" )
    private val halfToneKeys = listOf("C#", "D#", "F#", "G#", "A#", "CC#", "DD#", "FF#", "GG#", "AA#","CCC#", "DDD#", "FFF#", "GGG#", "AAA#")
    private val halfToneKeyPositions = mutableListOf(1, 2, 4, 5, 6, 8, 9, 11, 12, 13, 15, 16, 18, 19, 20)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPianoBinding.inflate(layoutInflater)

        val view = binding.root
        val fm = childFragmentManager
        val ft = fm.beginTransaction()

        fullToneKeys.forEach{
            val fullTonePianoKey = FullTonePianoKeyFragment.newInstance(it)

            fullTonePianoKey.onKeyDown = {
                println("Full tone key down $it")
            }
            fullTonePianoKey.onKeyUp = {
                println("Full tone key up $it")
            }

            ft.add(view.fullToneKeyLayout.id, fullTonePianoKey, "note_$it")
        }

        halfToneKeys.forEach{
            val displayMetric = context?.resources?.displayMetrics
            val densityDPI = displayMetric!!.densityDpi.toFloat()
            val halfToneKeyPosition = halfToneKeyPositions(fullToneKeyWidth = 35f,
                    densityDPI,
                    position = halfToneKeyPositions.removeAt(0),
                    padding = 0.8f,
                    halfToneKeywidth = 25f)
            val halfTonePianoKey = HalfTonePianoKeyFragment.newInstance(it, halfToneKeyPosition)


            halfTonePianoKey.onKeyDown = {
                println("Half tone key down $it")
            }
            halfTonePianoKey.onKeyUp = {
                println("Half tone key up $it")
            }
            ft.add(view.halfToneKeyLayout.id, halfTonePianoKey, "note_$it")
        }

        ft.commit()

        return view
    }

}