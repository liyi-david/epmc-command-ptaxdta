/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package epmc.udbm;

public class VarNamesAccessor {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected VarNamesAccessor(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(VarNamesAccessor obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        udbm_intJNI.delete_VarNamesAccessor(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setClockName(int index, String name) {
    udbm_intJNI.VarNamesAccessor_setClockName(swigCPtr, this, index, name);
  }

  public VarNamesAccessor() {
    this(udbm_intJNI.new_VarNamesAccessor(), true);
  }

}
