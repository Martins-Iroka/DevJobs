package com.martdev.android.devjobs.devjobresult

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.martdev.android.devjobs.DevJobFactory
import com.martdev.android.devjobs.R
import com.martdev.android.devjobs.databinding.DevjobsResultFragmentBinding

class DevJobResultFragment : Fragment() {

    private lateinit var viewModel: DevJobResultVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<DevjobsResultFragmentBinding>(inflater,
                R.layout.devjobs_result_fragment, container, false)

        val keyword = DevJobResultFragmentArgs.fromBundle(arguments!!).keyword
        val factory = DevJobFactory(keyword = keyword)
        viewModel = ViewModelProviders.of(this, factory)[DevJobResultVM::class.java]

        binding.lifecycleOwner = this

        binding.devJobVM = viewModel

        binding.devjobRecyclerView.adapter = DevJobAdapter(DevJobAdapter.OnClickListener {
            viewModel.showJobDetails(it)
        })

        viewModel.navigateToJobDetail.observe(this, Observer {
            if (it != null) {
                this.findNavController().navigate(DevJobResultFragmentDirections.actionDevJobResultFragmentToDevJobDetail(it))
                viewModel.jobDetailShown()
            }
        })
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.keyword_search, menu)

        val searchItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView

        query(searchView)

        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun query(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return if (query != null) {
                    viewModel.searchForJob(query)
                    true
                } else false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }
}