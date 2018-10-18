package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

//继承service
public class PedometerService extends Service {

    private IPedometerService.Stub iPedometerService =  new IPedometerService.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void startCount() throws RemoteException {

        }

        @Override
        public void stopCount() throws RemoteException {

        }

        @Override
        public void resetCount() throws RemoteException {

        }

        @Override
        public int getSetpsCount() throws RemoteException {
            return 0;
        }

        @Override
        public double getCarlorie() throws RemoteException {
            return 0;
        }

        @Override
        public double getDistance() throws RemoteException {
            return 0;
        }

        @Override
        public void saveData() throws RemoteException {

        }

        @Override
        public void setSensitivity(double sensitivity) throws RemoteException {

        }

        @Override
        public double getSensitivity() throws RemoteException {
            return 0;
        }

        @Override
        public void setInterval(int interval) throws RemoteException {

        }

        @Override
        public int getInterval() throws RemoteException {
            return 0;
        }

        @Override
        public long getStartTimeStamp() throws RemoteException {
            return 0;
        }

        @Override
        public int getServiceStatus() throws RemoteException {
            return 0;
        }

    };



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
