/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package epmc.udbm;

public class DoubleClockValuation {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected DoubleClockValuation(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(DoubleClockValuation obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        udbm_intJNI.delete_DoubleClockValuation(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public DoubleClockValuation(int size) {
    this(udbm_intJNI.new_DoubleClockValuation(size), true);
  }

  public void setClockValue(int index, double arg1) {
    udbm_intJNI.DoubleClockValuation_setClockValue(swigCPtr, this, index, arg1);
  }

}
