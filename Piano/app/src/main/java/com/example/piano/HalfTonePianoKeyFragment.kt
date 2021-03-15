package com.example.piano

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.piano.databinding.FragmentHalfTonePianoKeyBinding
import kotlinx.android.synthetic.main.fragment_half_tone_piano_key.view.*
import kotlinx.android.synthetic.main.fragment_piano.view.*

private lateinit var note:String

class HalfTonePianoKeyFragment : Fragment() {
    private var _binding: FragmentHalfTonePianoKeyBinding? = null
    private val binding get() = _binding!!
    private lateinit var note:String

    var halfKeyPosition:Float = 0f
    var onKeyDown:((note:String)-> Unit)? = null
    var onKeyUp:((note:String)-> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let{
            note = it.getString("NOTE") ?:"?"
            halfKeyPosition = it.getFloat("HALFKEYPOSITION")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHalfTonePianoKeyBinding.inflate(inflater)
        val view = binding.root
        view.sharpPianoKeyLayout.translationX = halfKeyPosition

        view.halfToneKey.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action){
                    MotionEvent.ACTION_DOWN -> this@HalfTonePianoKeyFragment.onKeyDown?.invoke(note)
                    MotionEvent.ACTION_UP -> this@HalfTonePianoKeyFragment.onKeyUp?.invoke(note)
                }
                return true
            }
        })

        return view
    }

    companion object{
        fun newInstance(note: String, halfKeyPosition: Float)=
            HalfTonePianoKeyFragment().apply {
                arguments = Bundle().apply{
                    putString("NOTE",note)
                    putFloat("HALFKEYPOSITION", halfKeyPosition)
                }
            }
    }
}