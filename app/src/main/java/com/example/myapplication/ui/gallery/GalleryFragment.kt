
package com.example.myapplication.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentGalleryBinding

@Suppress("DEPRECATION")
class GalleryFragment : Fragment() {

    lateinit var button: Button
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!


    @Deprecated("Deprecated in Java")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }
}