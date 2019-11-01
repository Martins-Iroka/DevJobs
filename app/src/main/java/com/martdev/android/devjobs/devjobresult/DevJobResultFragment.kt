package com.martdev.android.devjobs.devjobresult

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.martdev.android.devjobs.R
import com.martdev.android.devjobs.data.Result
import com.martdev.android.devjobs.databinding.DevjobsResultFragmentBinding
import com.martdev.android.devjobs.util.ConnectivityUtil
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.devjobs_result_fragment.*
import javax.inject.Inject

class DevJobResultFragment : DaggerFragment() {

    private val arg by navArgs<DevJobResultFragmentArgs>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: DevJobResultVM by viewModels { viewModelFactory }

    private lateinit var binding: DevjobsResultFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.devjobs_result_fragment, container, false)

        binding.lifecycleOwner = this.viewLifecycleOwner

        binding.devJobVM = viewModel

        setupRecyclerView(binding)

        viewModel.showResult(arg.keyword)

        pageListObserver()

        navigationObserver()

        networkStateObserver()

        checkNetwork()
        binding.swipe.refresh()
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun setupRecyclerView(binding: DevjobsResultFragmentBinding) {
        binding.devjobRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        binding.devjobRecyclerView.adapter = DevJobAdapter(DevJobAdapter.OnClickListener {
            viewModel.showJobDetails(it)
        })
    }

    private fun checkNetwork() {
        val isConnected = ConnectivityUtil.isConnected(context!!)
        if (isConnected) {
            viewModel.connectivityAvailable = isConnected
        } else {
            Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show()
        }
    }

    private fun networkStateObserver() {
        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it.status == Result.Status.ERROR) {
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigationObserver() {
        viewModel.navigateToJobDetail.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController().navigate(DevJobResultFragmentDirections.actionDevJobResultFragmentToDevJobDetail(it))
                viewModel.jobDetailShown()
            }
        })
    }

    private fun pageListObserver() {
        viewModel.devJobs.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) no_data_message.visibility = View.VISIBLE
            else no_data_message.visibility = View.GONE
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.keyword_search, menu)

        val searchItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView

        searchView.query()

        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun SearchView.query() {
        this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return if (query != null) {
                    checkNetwork()
                    viewModel.searchForJob(query)
                    this@query.onActionViewCollapsed()
                    activity?.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
                    true
                } else false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun SwipeRefreshLayout.refresh() {
        this.setOnRefreshListener {
            checkNetwork()
            val keyword = viewModel.searchKeyword.value
            keyword?.let { viewModel.showResult(it) }
        }
    }
}