package com.wx.io.save;

import com.wx.crypto.CryptoException;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class helps dealing with any kind of saveable file by providing callback
 * methods (see {@link SaveHandler} in case of error. This class provides the
 * following elements:<br>
 * <ul>
 * <li>Usual save method: {@link #save()}</li>
 * <li>A silent save method that will try to save, in case of an error, the
 * {@link SaveHandler}* will be triggered: {@link #trySave() }</li>
 * <li>A method to notify a change in the file: {@link #notifyChanged() }</li>
 * </ul>
 * <br>
 * This also provides the {@link #isSaved()} method that indicates its state.
 * Note that this information is not taken in account when {@link #save()} is
 * called. Actually, {@code save()} is equivalent to calling:<br><br>
 * {@code notifiyChanged();}<br>
 * {@code save0()}<br><br>
 * In order to avoid saving an already up-to-date file, the user is free
 * to check the {@link #isSaved()} method before calling {@link #save()}
 * <br><br>
 * * Note that if no {@code SaveHandler} is set, the class handler will be
 * used. If no class handler is set neither, the {@link #trySave()} method will
 * have no effect.
 *
 * @author Canale
 */
public abstract class SaveableConfig {

    private SaveHandler objectHandler;
    private boolean isSaved = true;

    public void setErrorHandler(SaveHandler handler) {
        this.objectHandler = handler;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public boolean trySave() {
        notifyChanged();
        SaveHandler handler = objectHandler == null
                ? SaveHandler.getHandler(getClass()) : objectHandler;
        if (handler != null) {
            try {
                save();
                return true;
            } catch (FileNotFoundException ex) {
                handler.handle(this, ex);
            } catch (IOException ex) {
                handler.handle(this, ex);
            } catch (CryptoException ex) {
                handler.handle(this, ex);
            }
        }
        return false;
    }

    public void save() throws IOException, CryptoException {
        notifyChanged();
        save0();
        isSaved = true;
    }

    protected void notifyChanged() {
        isSaved = false;
    }

    protected abstract void save0() throws IOException, CryptoException;
}
