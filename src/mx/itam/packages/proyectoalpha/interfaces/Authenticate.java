package mx.itam.packages.proyectoalpha.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Authenticate extends Remote {
    public String[] authenticate(String user) throws RemoteException;
}
