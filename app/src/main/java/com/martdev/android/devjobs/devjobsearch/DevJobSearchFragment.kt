package com.martdev.android.devjobs.devjobsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.martdev.android.devjobs.R
import com.martdev.android.devjobs.databinding.DevjobsSearchFragmentBinding

class DevJobSearchFragment : Fragment() {

    private val viewModel: DevJobSearchVM by lazy {
        ViewModelProviders.of(this)[DevJobSearchVM::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<DevjobsSearchFragmentBinding>(inflater,
                R.layout.devjobs_search_fragment, container, false)

        binding.devJobSearch = viewModel

        binding.lifecycleOwner = this

        viewModel.showNoEntry.observe(this, Observer {
            if (it == true) {
                Snackbar.make(binding.root, "Enter a keyword", Snackbar.LENGTH_LONG).show()
                viewModel.disableLiveData()
            }
        })

        viewModel.navigateToResult.observe(this, Observer {
            if (it == true) {
                this.findNavController()
                        .navigate(DevJobSearchFragmentDirections
                                .actionDevJobSearchFragmentToDevJobResultFragment(viewModel.searchKeyword.value!!))
                viewModel.disableLiveData()
            }
        })
        return binding.root
    }
}