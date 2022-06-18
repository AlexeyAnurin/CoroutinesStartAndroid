package com.example.coroutines.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.util.TimeUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coroutines.R
import com.example.coroutines.databinding.MainFragmentBinding
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.*

const val TAG = "Anurin"

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    private lateinit var job: Job
    private lateinit var job2: Job
    private lateinit var parentJob: Job
    private lateinit var deferred: Deferred<String>

    private lateinit var viewModel: MainViewModel
    private val scope = CoroutineScope(Job())
    private val scope2 = CoroutineScope(Job())
    private var formatter = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //! Job - это scope, лежит как Job в этом scope. УТОЧНИТЬ
        /*val job = scope.launch {
            Log.d("Anurin", "this.coroutineContext.job = ${this.coroutineContext.job}")
            Log.d("Anurin", "scope = $this")
        }
        Log.d("Anurin", "job = $job")*/

        _binding = MainFragmentBinding.inflate(inflater, container, false)

        binding.buttonRun.setOnClickListener {
            onRun()

        }
        binding.buttonRun2.setOnClickListener {
            onRun2()

        }
        binding.buttonCancel.setOnClickListener {
            onCancel()
        }
        //! Не передали Job. Она была создана самостоятельно.
        //! Job = JobImpl{Active}@dadc3da, Dispatcher = null
        val emptyCoroutineContext = EmptyCoroutineContext //! не содержит Jobx
        val scope = CoroutineScope(emptyCoroutineContext)
        //!Log.d(TAG, "scope, ${contextToString(scope.coroutineContext)}")
        Log.d(TAG,"coroutineContext scope, ${scope.coroutineContext}")

        //! Использован диспетчер по умолчанию
        //! Job = StandaloneCoroutine{Active}@be68ee8, Dispatcher = Dispatchers.Default
        scope.launch {
            //!Log.d(TAG,"coroutine parent, ${contextToString(kotlin.coroutines.coroutineContext)}")
            Log.d(TAG,"coroutineContext parent, ${kotlin.coroutines.coroutineContext.job}")

            launch {
                Log.d(TAG,"coroutineContext child, ${kotlin.coroutines.coroutineContext}")
            }
        }

        val view = binding.root
        return view
    }

    fun onRun() {

    }

    fun onRun2() {

    }

    private fun contextToString(context: CoroutineContext): String =
        "Job = ${context[Job]}, Dispatcher = ${context[ContinuationInterceptor]}"

    private fun onCancel() {
        Log.d("Anurin", "onCancel")
        job.cancel()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel

    }

    private suspend fun getData(): String {
        delay(1000)
        return "data"
    }

    private suspend fun getData2(): String {
        delay(1500)
        return "data2"
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    data class UserData(
        val yyy: Int,
        val zzz: Int
    ): AbstractCoroutineContextElement(UserData) {
        companion object Key : CoroutineContext.Key<UserData>
    }

}