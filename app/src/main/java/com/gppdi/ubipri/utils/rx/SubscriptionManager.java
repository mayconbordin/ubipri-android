package com.gppdi.ubipri.utils.rx;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.observables.Assertions;
import rx.functions.Func1;
import rx.operators.OperatorConditionalBinding;
import rx.subscriptions.CompositeSubscription;

import static rx.android.schedulers.AndroidSchedulers.mainThread;

/**
 * A class to help manage subscriptions. Subscriptions are automatically observed on the main
 * thread, and it will automatically unsubscribe any subscription if the predicate fails to
 * validate.
 */
public abstract class SubscriptionManager<T> {
    private final T instance;

    private final Func1<T, Boolean> predicate = new Func1<T, Boolean>() {
        @Override
        public Boolean call(T object) {
            return validate(object);
        }
    };

    private final CompositeSubscription subscriptions = new CompositeSubscription();

    public SubscriptionManager(T instance) {
        this.instance = instance;
    }

    /**
     * Returns a reference to the subscription in case you want to unsubscribe eagerly.
     */
    public <O> Subscription subscribe(final Observable<O> source, final Observer<O> observer) {
        Assertions.assertUiThread();
        Subscription subscription = source.observeOn(mainThread())
                .lift(new OperatorConditionalBinding<O, T>(instance, predicate))
                .subscribe(observer);
        subscriptions.add(subscription);
        return subscription;
    }

    public void unsubscribe(Subscription subscription) {
        subscriptions.remove(subscription);
    }

    public void unsubscribeAll() {
        subscriptions.unsubscribe();
    }

    /**
     * called by a {@link rx.functions.Func1} implementation that will be a predicate for {@link
     * rx.operators.OperatorConditionalBinding}. If the predicate fails to validate, the sequence
     * unsubscribes itself and releases the bound reference.
     */
    protected abstract boolean validate(final T object);
}