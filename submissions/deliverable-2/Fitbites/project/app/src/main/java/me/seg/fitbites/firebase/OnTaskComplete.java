package me.seg.fitbites.firebase;

public interface OnTaskComplete<T> {
    public void onComplete(T result);
}
