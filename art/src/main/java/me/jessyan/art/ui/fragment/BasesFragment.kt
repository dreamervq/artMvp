package com.example.mylibrary.mvp.uis.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.example.mylibrary.mvp.uis.activities.BaseActivity
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.AutoDisposeConverter
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.subjects.BehaviorSubject
import me.jessyan.art.R
import me.jessyan.art.base.BaseFragment
import me.jessyan.art.mvp.IPresenter
import me.jessyan.art.mvp.IView
import me.jessyan.art.ui.util.RxCheckLifeCycleTransformer.LifeCycleEvent
import me.jessyan.art.utils.ArtUtils

abstract class BasesFragment<P : IPresenter> : BaseFragment<P>(), IView {
    protected var eventBehaviorSubject = BehaviorSubject.create<LifeCycleEvent>()
    private var mLastButterKnifeClickTime: Long = 0

    override fun initData() {
        eventBehaviorSubject.onNext(LifeCycleEvent.CREATE_VIEW)
    }


    override fun onDestroyView() {
        eventBehaviorSubject.onNext(LifeCycleEvent.DESTROY_VIEW)
        super.onDestroyView()
    }

    override fun shortToast(message: String) {
        ArtUtils.makeText(mContext, message)
    }

    override fun shortToast(messageResId: Int) {
        shortToast(getString(messageResId))
    }

    @JvmOverloads
    open fun startActivity(clazz: Class<*>?, bundle: Bundle? = null) {
        val intent = Intent(mContext, clazz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.ui_right_in, R.anim.fade_out)
    }

    @JvmOverloads
    open fun startActivityForResult(clazz: Class<*>?, requestCode: Int, bundle: Bundle? = null) {
        val intent = Intent(mContext, clazz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, requestCode)
        activity?.overridePendingTransition(R.anim.ui_right_in, R.anim.fade_out)
    }

    abstract fun getContentViewId(): Int

    // abstract fun init(savedInstanceState: Bundle?)
    override fun onResume() {
        super.onResume()
        eventBehaviorSubject.onNext(LifeCycleEvent.RESUME)
    }

    protected fun mobResume() {
//        MobclickAgent.onPageStart(getClass().getSimpleName());//fragment页面统计
    }

    override fun onPause() {
        super.onPause()
        //        mobPause();
    }

    protected fun mobPause() {
//        MobclickAgent.onPageEnd(getClass().getSimpleName());//fragment页面统计
    }

    override fun onDestroy() {
        super.onDestroy()
        eventBehaviorSubject.onNext(LifeCycleEvent.DESTROY)
    }

    override fun onDetach() {
        super.onDetach()
        eventBehaviorSubject.onNext(LifeCycleEvent.DETACH)
    }

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getContentViewId(), null)
        return view
    }

    /**
     * 把Observable的生命周期与Activity绑定
     *
     * @param <D>
     * @return
    </D> */
    override fun <T> bindAutoDispose(): AutoDisposeConverter<T?>? {
        return AutoDispose.autoDisposable(
            AndroidLifecycleScopeProvider
                .from(this, Lifecycle.Event.ON_DESTROY)
        )
    }

    override fun showLoading() {
        showLoading(R.string.loading)
    }

    fun showLoading(res: Int) {
        showLoading(getString(res))
    }

    override fun showLoading(message: String?) {
        if (activity is BaseActivity<*>) {
            val baseActivity = activity as BaseActivity<*>?
            baseActivity?.showProgressDialog(message)
        }
    }

    override fun hideLoading() {
        if (activity is BaseActivity<*>) {
            val baseActivity = activity as BaseActivity<*>?
            baseActivity?.hideLoading()
        }
    }


    fun beFastClick(): Boolean {
        val currentClickTime = System.currentTimeMillis()
        val flag = currentClickTime - mLastButterKnifeClickTime < 400L
        mLastButterKnifeClickTime = currentClickTime
        return flag
    }

    override fun stateError() {
    }

    override fun showMessage(message: String) {
    }

    override fun setData(data: Any?) {
    }

    open fun getPresenter(): P {
        if (mPresenter == null) {
            mPresenter = obtainPresenter()
        }
        return mPresenter!!
    }

    open fun <T : View?> getView(id: Int): T {
        return if (rootView != null) {
            try {
                rootView.findViewById<T>(id)
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        } else try {
            activity!!.findViewById<T>(id)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}