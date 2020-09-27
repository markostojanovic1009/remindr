package rs.ac.bg.etf.remindr.common;

public class PersistenceRequest
{
    public boolean IsSuccess;
    public String Error;

    public PersistenceRequest()
    {
        IsSuccess = true;
    }

    public PersistenceRequest(String error)
    {
        Error = error;
        IsSuccess = false;
    }
}
