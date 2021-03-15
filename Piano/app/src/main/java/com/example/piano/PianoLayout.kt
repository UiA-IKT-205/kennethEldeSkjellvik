package com.example.piano

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.piano.data.Note
import com.example.piano.databinding.FragmentPianoBinding
import kotlinx.android.synthetic.main.fragment_piano.view.*
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class PianoLayout : Fragment() {
    private var _binding:FragmentPianoBinding? = null
    private val binding get() = _binding!!

    private val fullToneKeys = listOf("C", "D", "E", "F", "G", "A", "B", "CC", "DD", "EE", "FF", "GG", "AA", "BB", "CCC", "DDD", "EEE", "FFF", "GGG", "AAA", "BBB" )
    private val halfToneKeys = listOf("C#", "D#", "F#", "G#", "A#", "CC#", "DD#", "FF#", "GG#", "AA#","CCC#", "DDD#", "FFF#", "GGG#", "AAA#")
    private val halfToneKeyPositions = mutableListOf(1, 2, 4, 5, 6, 8, 9, 11, 12, 13, 15, 16, 18, 19, 20)
    private var score:MutableList<Note> = mutableListOf<Note>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPianoBinding.inflate(layoutInflater)

        val view = binding.root
        val fm = childFragmentManager
        val ft = fm.beginTransaction()

        fullToneKeys.forEach{ it ->
            val fullTonePianoKey = FullTonePianoKeyFragment.newInstance(it)
            var startPlay:Long = 0
            var fullToneStartTime:String = ""

            fullTonePianoKey.onKeyDown = {
                startPlay = System.currentTimeMillis()

                val currentTime = LocalDateTime.now()
                fullToneStartTime = currentTime.format(DateTimeFormatter.ISO_TIME)
                println("Full tone key down $it")
            }
            fullTonePianoKey.onKeyUp = {
                val endPlay = System.currentTimeMillis()
                var fullToneTotalTime:Double = 0.0
                var fullToneTime:Long = 0

                fullToneTime = endPlay - startPlay
                fullToneTotalTime = fullToneTime.toDouble()

                val note = Note(it, fullToneStartTime, fullToneTotalTime)
                score.add(note)
                println("Piano key up $note. Started at $fullToneStartTime. Duration $fullToneTotalTime milliseconds")
            }

            ft.add(view.fullToneKeyLayout.id, fullTonePianoKey, "note_$it")
        }

        halfToneKeys.forEach{ it ->
            val displayMetric = context?.resources?.displayMetrics
            val densityDPI = displayMetric!!.densityDpi.toFloat()
            val halfToneKeyPosition = halfToneKeyPositions(fullToneKeyWidth = 35f,
                    densityDPI,
                    position = halfToneKeyPositions.removeAt(0),
                    padding = 0.8f,
                    halfToneKeywidth = 25f)
            val halfTonePianoKey = HalfTonePianoKeyFragment.newInstance(it, halfToneKeyPosition)
            var startPlay:Long = 0
            var halfToneStartTime:String = ""

            halfTonePianoKey.onKeyDown = {
                startPlay = System.currentTimeMillis()
                val currentTime = LocalDateTime.now()
                halfToneStartTime = currentTime.format(DateTimeFormatter.ISO_TIME)
                println("Half tone key down $it")
            }
            halfTonePianoKey.onKeyUp = {
                val endPlay = System.currentTimeMillis()
                var halfToneTotalTime:Double = 0.0
                var halfToneTime:Long = 0

                halfToneTime = endPlay - startPlay
                halfToneTotalTime = halfToneTime.toDouble()

                val note = Note(it, halfToneStartTime, halfToneTotalTime)
                score.add(note)
                println("Piano key up $note. Started at $halfToneStartTime. Duration $halfToneTotalTime milliseconds")
            }
            ft.add(view.halfToneKeyLayout.id, halfTonePianoKey, "note_$it")
        }

        ft.commit()

        view.saveScoreBt.setOnClickListener {
            var fileName = view.fileNameTextEdit.text.toString()
            val path = this.activity?.getExternalFilesDir(null)
            val newNoteFile = (File(path, fileName))

            when {
                score.count() == 0 -> Toast.makeText(activity, "No notes has been pressed, so there is nothing to be saved", Toast.LENGTH_SHORT).show()
                fileName.isEmpty() -> Toast.makeText(activity, "The file you are trying to save needs a name", Toast.LENGTH_SHORT).show()
                path == null -> Toast.makeText(activity, "The path does not exist", Toast.LENGTH_SHORT).show()
                newNoteFile.exists() -> Toast.makeText(activity, "The filename you are trying to save already exists", Toast.LENGTH_SHORT).show()


                else -> {
                    fileName = "$fileName.music"
                    FileOutputStream(newNoteFile, true).bufferedWriter().use{writer ->
                    score.forEach{
                        writer.write ("$it\n")
                    }
                        FileOutputStream(newNoteFile).close()
                        score.clear()
                    }
                    Toast.makeText(activity, "Saved the file with the name $fileName", Toast.LENGTH_SHORT).show()
                    print("Saved as $fileName at $path/$fileName")
                }
            }
        }
        return view
    }

}