/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package epmc.udbm;

public class AtomConstraint {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected AtomConstraint(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(AtomConstraint obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        udbm_intJNI.delete_AtomConstraint(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public AtomConstraint(int i, int j, int d, boolean isStrict) {
    this(udbm_intJNI.new_AtomConstraint(i, j, d, isStrict), true);
  }

}
