package rs.ac.bg.etf.remindr.common;

public class NetworkRequest<T> {
    private T item_;
    private String error_;
    private boolean isSuccess_;

    public NetworkRequest(T item)
    {
        item_ = item;
        error_ = "";
        isSuccess_ = true;
    }

    public NetworkRequest(T item, String error)
    {
        item_ = item;
        error_ = error;
        isSuccess_ = false;
    }

    public String GetError()
    {
        return error_;
    }

    public boolean IsSuccess()
    {
        return isSuccess_;
    }

    public T GetItem()
    {
        return item_;
    }
}
