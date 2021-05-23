package ru.spbstu.commons

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.spbstu.commons.databinding.FragmentBaseRecyclerBinding

abstract class BaseRecyclerFragment : Fragment(R.layout.fragment_base_recycler) {

    protected val recyclerBinding: FragmentBaseRecyclerBinding by viewBinding()

    protected open val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder> by lazyUnsychronized {
        createRecyclerAdapter()
    }

    protected val recyclerLayoutManager by lazyUnsychronized {
        createRecyclerLayoutManager()
    }
    protected lateinit var recyclerView: RecyclerView

    protected abstract fun createRecyclerAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>

    protected fun createRecyclerView(): RecyclerView = recyclerBinding.list

    protected fun createRecyclerLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()
    }

    protected fun initRecyclerView() {
        recyclerView = createRecyclerView().apply {
            layoutManager = recyclerLayoutManager
            setHasFixedSize(true)
            this.adapter = this@BaseRecyclerFragment.adapter
        }
        recyclerBinding.swipeRefresh.setOnRefreshListener {
            onRefresh()
        }
    }

    protected open fun onRefresh() {
    }
/* TODO
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (recyclerView != null) {
            outState.putParcelable(
                BaseRefreshRecyclerFragment.STATE_RECYCLER_SCROLL,
                recyclerView.getLayoutManager().onSaveInstanceState()
            )
        }
    }*/
}