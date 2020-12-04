package me.jessyan.art.ui.util

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.BehaviorSubject

/**
 * 用于处理RxJava回调的生命周期，把Activity和Fragment的生命周期和[LifeCycleEvent]绑定
 */
class RxCheckLifeCycleTransformer<T>(eventBehavior: BehaviorSubject<LifeCycleEvent>?) :
    ObservableTransformer<T, T> {
    var eventBehavior: BehaviorSubject<LifeCycleEvent>
    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream.takeUntil(eventBehavior.skipWhile { event -> event != LifeCycleEvent.DESTROY && event != LifeCycleEvent.DETACH && event != LifeCycleEvent.DESTROY_VIEW })
    }

    enum class LifeCycleEvent {
        // Activity Events
        CREATE, START, RESUME, PAUSE, STOP, DESTROY,  // Fragment Events
        ATTACH, CREATE_VIEW, DESTROY_VIEW, DETACH
    }

    init {
        if (eventBehavior == null) {
            throw RuntimeException("eventBehavior can not be null")
        }
        this.eventBehavior = eventBehavior
    }
}