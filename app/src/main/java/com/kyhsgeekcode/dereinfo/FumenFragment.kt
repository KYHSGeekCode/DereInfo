package com.kyhsgeekcode.dereinfo

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kyhsgeekcode.dereinfo.model.DereDatabaseHelper
import com.kyhsgeekcode.dereinfo.model.MusicInfo
import com.kyhsgeekcode.dereinfo.model.TW5Difficulty
import kotlinx.android.synthetic.main.fumen_fragment.view.*

class FumenFragment : Fragment() {

    companion object {
        fun newInstance(musicInfo: MusicInfo, tW5Difficulty: TW5Difficulty) =
            FumenFragment().apply {
                val args = Bundle()
                args.putSerializable("musicInfo", musicInfo)
                args.putSerializable("tw5Difficulty", tW5Difficulty)
                arguments = args
            }
    }

    private lateinit var viewModel: FumenViewModel

    private var bitmap: Bitmap? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fumen_fragment, container, false)
        val context = requireContext()
        val musicInfo: MusicInfo
        val tw5Difficulty: TW5Difficulty
        arguments!!.let {
            musicInfo = it.getSerializable("musicInfo") as MusicInfo
            tw5Difficulty = it.getSerializable("tw5Difficulty") as TW5Difficulty
            Log.d("TAG", "musicInfo:$musicInfo, tw5Difficulty:$tw5Difficulty")
        }
        //FumenRenderer(5).render(DereDatabaseHelper.theInstance.parsed)
        val oneDifficulty =
            DereDatabaseHelper.theInstance.parsedFumenCache[Pair(
                musicInfo.id,
                tw5Difficulty
            )]?.difficulties?.get(tw5Difficulty)
        if (oneDifficulty == null) {
            Toast.makeText(
                requireActivity(),
                "Failed to get the Difficulty",
                Toast.LENGTH_SHORT
            ).show()
//            parentFragmentManager.popBackStackImmediate()
            return root
        }
        bitmap =
            FumenRenderer(
                context,
                oneDifficulty.lanes
            ).render(oneDifficulty)
        if (bitmap == null) {
            Toast.makeText(
                requireActivity(),
                "Failed to render",
                Toast.LENGTH_SHORT
            ).show()
            return root
        }
        Log.d("TAG", "Bitmap: $bitmap")
        root.pv_fumen.setImageBitmap(bitmap)
//        saveImage(bitmap!!, requireContext(), "dereinfo")
        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        bitmap?.recycle()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FumenViewModel::class.java)
        // TODO: Use the ViewModel
    }

}