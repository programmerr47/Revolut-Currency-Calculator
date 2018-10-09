package com.github.programmerr47.currencycalculator.util

import io.reactivex.Observable
import io.reactivex.Observer

//since subscribing subject does not returns Disposable we need to use that workaround
//here is issue: https://github.com/ReactiveX/RxJava/issues/4438
fun <T> Observable<T>.subscribeCancelable(observer: Observer<T>) = subscribe(
        observer::onNext,
        observer::onError,
        observer::onComplete
)