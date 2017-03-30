/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * This file is not intended to be easily readable and contains a number of
 * coding conventions designed to improve portability and efficiency. Do not make
 * changes to this file unless you know what you are doing--modify the SWIG
 * interface file instead.
 * ----------------------------------------------------------------------------- */


#ifndef SWIGJAVA
#define SWIGJAVA
#endif



#ifdef __cplusplus
/* SwigValueWrapper is described in swig.swg */
template<typename T> class SwigValueWrapper {
  struct SwigMovePointer {
    T *ptr;
    SwigMovePointer(T *p) : ptr(p) { }
    ~SwigMovePointer() { delete ptr; }
    SwigMovePointer& operator=(SwigMovePointer& rhs) { T* oldptr = ptr; ptr = 0; delete oldptr; ptr = rhs.ptr; rhs.ptr = 0; return *this; }
  } pointer;
  SwigValueWrapper& operator=(const SwigValueWrapper<T>& rhs);
  SwigValueWrapper(const SwigValueWrapper<T>& rhs);
public:
  SwigValueWrapper() : pointer(0) { }
  SwigValueWrapper& operator=(const T& t) { SwigMovePointer tmp(new T(t)); pointer = tmp; return *this; }
  operator T&() const { return *pointer.ptr; }
  T *operator&() { return pointer.ptr; }
};

template <typename T> T SwigValueInit() {
  return T();
}
#endif

/* -----------------------------------------------------------------------------
 *  This section contains generic SWIG labels for method/variable
 *  declarations/attributes, and other compiler dependent labels.
 * ----------------------------------------------------------------------------- */

/* template workaround for compilers that cannot correctly implement the C++ standard */
#ifndef SWIGTEMPLATEDISAMBIGUATOR
# if defined(__SUNPRO_CC) && (__SUNPRO_CC <= 0x560)
#  define SWIGTEMPLATEDISAMBIGUATOR template
# elif defined(__HP_aCC)
/* Needed even with `aCC -AA' when `aCC -V' reports HP ANSI C++ B3910B A.03.55 */
/* If we find a maximum version that requires this, the test would be __HP_aCC <= 35500 for A.03.55 */
#  define SWIGTEMPLATEDISAMBIGUATOR template
# else
#  define SWIGTEMPLATEDISAMBIGUATOR
# endif
#endif

/* inline attribute */
#ifndef SWIGINLINE
# if defined(__cplusplus) || (defined(__GNUC__) && !defined(__STRICT_ANSI__))
#   define SWIGINLINE inline
# else
#   define SWIGINLINE
# endif
#endif

/* attribute recognised by some compilers to avoid 'unused' warnings */
#ifndef SWIGUNUSED
# if defined(__GNUC__)
#   if !(defined(__cplusplus)) || (__GNUC__ > 3 || (__GNUC__ == 3 && __GNUC_MINOR__ >= 4))
#     define SWIGUNUSED __attribute__ ((__unused__))
#   else
#     define SWIGUNUSED
#   endif
# elif defined(__ICC)
#   define SWIGUNUSED __attribute__ ((__unused__))
# else
#   define SWIGUNUSED
# endif
#endif

#ifndef SWIG_MSC_UNSUPPRESS_4505
# if defined(_MSC_VER)
#   pragma warning(disable : 4505) /* unreferenced local function has been removed */
# endif
#endif

#ifndef SWIGUNUSEDPARM
# ifdef __cplusplus
#   define SWIGUNUSEDPARM(p)
# else
#   define SWIGUNUSEDPARM(p) p SWIGUNUSED
# endif
#endif

/* internal SWIG method */
#ifndef SWIGINTERN
# define SWIGINTERN static SWIGUNUSED
#endif

/* internal inline SWIG method */
#ifndef SWIGINTERNINLINE
# define SWIGINTERNINLINE SWIGINTERN SWIGINLINE
#endif

/* exporting methods */
#if defined(__GNUC__)
#  if (__GNUC__ >= 4) || (__GNUC__ == 3 && __GNUC_MINOR__ >= 4)
#    ifndef GCC_HASCLASSVISIBILITY
#      define GCC_HASCLASSVISIBILITY
#    endif
#  endif
#endif

#ifndef SWIGEXPORT
# if defined(_WIN32) || defined(__WIN32__) || defined(__CYGWIN__)
#   if defined(STATIC_LINKED)
#     define SWIGEXPORT
#   else
#     define SWIGEXPORT __declspec(dllexport)
#   endif
# else
#   if defined(__GNUC__) && defined(GCC_HASCLASSVISIBILITY)
#     define SWIGEXPORT __attribute__ ((visibility("default")))
#   else
#     define SWIGEXPORT
#   endif
# endif
#endif

/* calling conventions for Windows */
#ifndef SWIGSTDCALL
# if defined(_WIN32) || defined(__WIN32__) || defined(__CYGWIN__)
#   define SWIGSTDCALL __stdcall
# else
#   define SWIGSTDCALL
# endif
#endif

/* Deal with Microsoft's attempt at deprecating C standard runtime functions */
#if !defined(SWIG_NO_CRT_SECURE_NO_DEPRECATE) && defined(_MSC_VER) && !defined(_CRT_SECURE_NO_DEPRECATE)
# define _CRT_SECURE_NO_DEPRECATE
#endif

/* Deal with Microsoft's attempt at deprecating methods in the standard C++ library */
#if !defined(SWIG_NO_SCL_SECURE_NO_DEPRECATE) && defined(_MSC_VER) && !defined(_SCL_SECURE_NO_DEPRECATE)
# define _SCL_SECURE_NO_DEPRECATE
#endif

/* Deal with Apple's deprecated 'AssertMacros.h' from Carbon-framework */
#if defined(__APPLE__) && !defined(__ASSERT_MACROS_DEFINE_VERSIONS_WITHOUT_UNDERSCORES)
# define __ASSERT_MACROS_DEFINE_VERSIONS_WITHOUT_UNDERSCORES 0
#endif

/* Intel's compiler complains if a variable which was never initialised is
 * cast to void, which is a common idiom which we use to indicate that we
 * are aware a variable isn't used.  So we just silence that warning.
 * See: https://github.com/swig/swig/issues/192 for more discussion.
 */
#ifdef __INTEL_COMPILER
# pragma warning disable 592
#endif


/* Fix for jlong on some versions of gcc on Windows */
#if defined(__GNUC__) && !defined(__INTEL_COMPILER)
  typedef long long __int64;
#endif

/* Fix for jlong on 64-bit x86 Solaris */
#if defined(__x86_64)
# ifdef _LP64
#   undef _LP64
# endif
#endif

#include <jni.h>
#include <stdlib.h>
#include <string.h>


/* Support for throwing Java exceptions */
typedef enum {
  SWIG_JavaOutOfMemoryError = 1, 
  SWIG_JavaIOException, 
  SWIG_JavaRuntimeException, 
  SWIG_JavaIndexOutOfBoundsException,
  SWIG_JavaArithmeticException,
  SWIG_JavaIllegalArgumentException,
  SWIG_JavaNullPointerException,
  SWIG_JavaDirectorPureVirtual,
  SWIG_JavaUnknownError
} SWIG_JavaExceptionCodes;

typedef struct {
  SWIG_JavaExceptionCodes code;
  const char *java_exception;
} SWIG_JavaExceptions_t;


static void SWIGUNUSED SWIG_JavaThrowException(JNIEnv *jenv, SWIG_JavaExceptionCodes code, const char *msg) {
  jclass excep;
  static const SWIG_JavaExceptions_t java_exceptions[] = {
    { SWIG_JavaOutOfMemoryError, "java/lang/OutOfMemoryError" },
    { SWIG_JavaIOException, "java/io/IOException" },
    { SWIG_JavaRuntimeException, "java/lang/RuntimeException" },
    { SWIG_JavaIndexOutOfBoundsException, "java/lang/IndexOutOfBoundsException" },
    { SWIG_JavaArithmeticException, "java/lang/ArithmeticException" },
    { SWIG_JavaIllegalArgumentException, "java/lang/IllegalArgumentException" },
    { SWIG_JavaNullPointerException, "java/lang/NullPointerException" },
    { SWIG_JavaDirectorPureVirtual, "java/lang/RuntimeException" },
    { SWIG_JavaUnknownError,  "java/lang/UnknownError" },
    { (SWIG_JavaExceptionCodes)0,  "java/lang/UnknownError" }
  };
  const SWIG_JavaExceptions_t *except_ptr = java_exceptions;

  while (except_ptr->code != code && except_ptr->code)
    except_ptr++;

  jenv->ExceptionClear();
  excep = jenv->FindClass(except_ptr->java_exception);
  if (excep)
    jenv->ThrowNew(excep, msg);
}


/* Contract support */

#define SWIG_contract_assert(nullreturn, expr, msg) if (!(expr)) {SWIG_JavaThrowException(jenv, SWIG_JavaIllegalArgumentException, msg); return nullreturn; } else


#define SWIG_FILE_WITH_INIT
#include "udbm_int.h"


#include <string>


#include <typeinfo>
#include <stdexcept>


#include <vector>
#include <stdexcept>


#ifdef __cplusplus
extern "C" {
#endif

SWIGEXPORT void JNICALL Java_epmc_udbm_udbm_1intJNI_VarNamesAccessor_1setClockName(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jint jarg2, jstring jarg3) {
  VarNamesAccessor *arg1 = (VarNamesAccessor *) 0 ;
  int arg2 ;
  std::string arg3 ;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  arg1 = *(VarNamesAccessor **)&jarg1; 
  arg2 = (int)jarg2; 
  if(!jarg3) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null string");
    return ;
  } 
  const char *arg3_pstr = (const char *)jenv->GetStringUTFChars(jarg3, 0); 
  if (!arg3_pstr) return ;
  (&arg3)->assign(arg3_pstr);
  jenv->ReleaseStringUTFChars(jarg3, arg3_pstr); 
  (arg1)->setClockName(arg2,arg3);
}


SWIGEXPORT jlong JNICALL Java_epmc_udbm_udbm_1intJNI_new_1VarNamesAccessor(JNIEnv *jenv, jclass jcls) {
  jlong jresult = 0 ;
  VarNamesAccessor *result = 0 ;
  
  (void)jenv;
  (void)jcls;
  result = (VarNamesAccessor *)new VarNamesAccessor();
  *(VarNamesAccessor **)&jresult = result; 
  return jresult;
}


SWIGEXPORT void JNICALL Java_epmc_udbm_udbm_1intJNI_delete_1VarNamesAccessor(JNIEnv *jenv, jclass jcls, jlong jarg1) {
  VarNamesAccessor *arg1 = (VarNamesAccessor *) 0 ;
  
  (void)jenv;
  (void)jcls;
  arg1 = *(VarNamesAccessor **)&jarg1; 
  delete arg1;
}


SWIGEXPORT jlong JNICALL Java_epmc_udbm_udbm_1intJNI_new_1AtomConstraint(JNIEnv *jenv, jclass jcls, jint jarg1, jint jarg2, jint jarg3, jboolean jarg4) {
  jlong jresult = 0 ;
  int arg1 ;
  int arg2 ;
  int arg3 ;
  bool arg4 ;
  AtomConstraint *result = 0 ;
  
  (void)jenv;
  (void)jcls;
  arg1 = (int)jarg1; 
  arg2 = (int)jarg2; 
  arg3 = (int)jarg3; 
  arg4 = jarg4 ? true : false; 
  result = (AtomConstraint *)new AtomConstraint(arg1,arg2,arg3,arg4);
  *(AtomConstraint **)&jresult = result; 
  return jresult;
}


SWIGEXPORT void JNICALL Java_epmc_udbm_udbm_1intJNI_delete_1AtomConstraint(JNIEnv *jenv, jclass jcls, jlong jarg1) {
  AtomConstraint *arg1 = (AtomConstraint *) 0 ;
  
  (void)jenv;
  (void)jcls;
  arg1 = *(AtomConstraint **)&jarg1; 
  delete arg1;
}


SWIGEXPORT jlong JNICALL Java_epmc_udbm_udbm_1intJNI_new_1IntClockValuation(JNIEnv *jenv, jclass jcls, jint jarg1) {
  jlong jresult = 0 ;
  int arg1 ;
  IntClockValuation *result = 0 ;
  
  (void)jenv;
  (void)jcls;
  arg1 = (int)jarg1; 
  result = (IntClockValuation *)new IntClockValuation(arg1);
  *(IntClockValuation **)&jresult = result; 
  return jresult;
}


SWIGEXPORT void JNICALL Java_epmc_udbm_udbm_1intJNI_IntClockValuation_1setClockValue(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jint jarg2, jint jarg3) {
  IntClockValuation *arg1 = (IntClockValuation *) 0 ;
  int arg2 ;
  int arg3 ;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  arg1 = *(IntClockValuation **)&jarg1; 
  arg2 = (int)jarg2; 
  arg3 = (int)jarg3; 
  (arg1)->setClockValue(arg2,arg3);
}


SWIGEXPORT void JNICALL Java_epmc_udbm_udbm_1intJNI_delete_1IntClockValuation(JNIEnv *jenv, jclass jcls, jlong jarg1) {
  IntClockValuation *arg1 = (IntClockValuation *) 0 ;
  
  (void)jenv;
  (void)jcls;
  arg1 = *(IntClockValuation **)&jarg1; 
  delete arg1;
}


SWIGEXPORT jlong JNICALL Java_epmc_udbm_udbm_1intJNI_new_1DoubleClockValuation(JNIEnv *jenv, jclass jcls, jint jarg1) {
  jlong jresult = 0 ;
  int arg1 ;
  DoubleClockValuation *result = 0 ;
  
  (void)jenv;
  (void)jcls;
  arg1 = (int)jarg1; 
  result = (DoubleClockValuation *)new DoubleClockValuation(arg1);
  *(DoubleClockValuation **)&jresult = result; 
  return jresult;
}


SWIGEXPORT void JNICALL Java_epmc_udbm_udbm_1intJNI_DoubleClockValuation_1setClockValue(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jint jarg2, jdouble jarg3) {
  DoubleClockValuation *arg1 = (DoubleClockValuation *) 0 ;
  int arg2 ;
  double arg3 ;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  arg1 = *(DoubleClockValuation **)&jarg1; 
  arg2 = (int)jarg2; 
  arg3 = (double)jarg3; 
  (arg1)->setClockValue(arg2,arg3);
}


SWIGEXPORT void JNICALL Java_epmc_udbm_udbm_1intJNI_delete_1DoubleClockValuation(JNIEnv *jenv, jclass jcls, jlong jarg1) {
  DoubleClockValuation *arg1 = (DoubleClockValuation *) 0 ;
  
  (void)jenv;
  (void)jcls;
  arg1 = *(DoubleClockValuation **)&jarg1; 
  delete arg1;
}


SWIGEXPORT jlong JNICALL Java_epmc_udbm_udbm_1intJNI_new_1IntVector(JNIEnv *jenv, jclass jcls, jint jarg1) {
  jlong jresult = 0 ;
  int arg1 ;
  IntVector *result = 0 ;
  
  (void)jenv;
  (void)jcls;
  arg1 = (int)jarg1; 
  result = (IntVector *)new IntVector(arg1);
  *(IntVector **)&jresult = result; 
  return jresult;
}


SWIGEXPORT void JNICALL Java_epmc_udbm_udbm_1intJNI_IntVector_1setElement(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jint jarg2, jint jarg3) {
  IntVector *arg1 = (IntVector *) 0 ;
  int arg2 ;
  int arg3 ;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  arg1 = *(IntVector **)&jarg1; 
  arg2 = (int)jarg2; 
  arg3 = (int)jarg3; 
  (arg1)->setElement(arg2,arg3);
}


SWIGEXPORT void JNICALL Java_epmc_udbm_udbm_1intJNI_delete_1IntVector(JNIEnv *jenv, jclass jcls, jlong jarg1) {
  IntVector *arg1 = (IntVector *) 0 ;
  
  (void)jenv;
  (void)jcls;
  arg1 = *(IntVector **)&jarg1; 
  delete arg1;
}


SWIGEXPORT jlong JNICALL Java_epmc_udbm_udbm_1intJNI_new_1Federation_1_1SWIG_10(JNIEnv *jenv, jclass jcls, jint jarg1) {
  jlong jresult = 0 ;
  int arg1 ;
  Federation *result = 0 ;
  
  (void)jenv;
  (void)jcls;
  arg1 = (int)jarg1; 
  result = (Federation *)new Federation(arg1);
  *(Federation **)&jresult = result; 
  return jresult;
}


SWIGEXPORT jlong JNICALL Java_epmc_udbm_udbm_1intJNI_new_1Federation_1_1SWIG_11(JNIEnv *jenv, jclass jcls, jint jarg1, jlong jarg2, jobject jarg2_) {
  jlong jresult = 0 ;
  int arg1 ;
  AtomConstraint *arg2 = 0 ;
  Federation *result = 0 ;
  
  (void)jenv;
  (void)jcls;
  (void)jarg2_;
  arg1 = (int)jarg1; 
  arg2 = *(AtomConstraint **)&jarg2;
  if (!arg2) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "AtomConstraint & reference is null");
    return 0;
  } 
  result = (Federation *)new Federation(arg1,*arg2);
  *(Federation **)&jresult = result; 
  return jresult;
}


SWIGEXPORT jlong JNICALL Java_epmc_udbm_udbm_1intJNI_new_1Federation_1_1SWIG_12(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_) {
  jlong jresult = 0 ;
  Federation *arg1 = 0 ;
  Federation *result = 0 ;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  arg1 = *(Federation **)&jarg1;
  if (!arg1) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "Federation const & reference is null");
    return 0;
  } 
  result = (Federation *)new Federation((Federation const &)*arg1);
  *(Federation **)&jresult = result; 
  return jresult;
}


SWIGEXPORT jstring JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1toStr(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_) {
  jstring jresult = 0 ;
  Federation *arg1 = (Federation *) 0 ;
  VarNamesAccessor *arg2 = 0 ;
  std::string result;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  (void)jarg2_;
  arg1 = *(Federation **)&jarg1; 
  arg2 = *(VarNamesAccessor **)&jarg2;
  if (!arg2) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "VarNamesAccessor & reference is null");
    return 0;
  } 
  result = (arg1)->toStr(*arg2);
  jresult = jenv->NewStringUTF((&result)->c_str()); 
  return jresult;
}


SWIGEXPORT jlong JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1orOp(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_) {
  jlong jresult = 0 ;
  Federation *arg1 = (Federation *) 0 ;
  Federation *arg2 = 0 ;
  SwigValueWrapper< Federation > result;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  (void)jarg2_;
  arg1 = *(Federation **)&jarg1; 
  arg2 = *(Federation **)&jarg2;
  if (!arg2) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "Federation & reference is null");
    return 0;
  } 
  result = (arg1)->orOp(*arg2);
  *(Federation **)&jresult = new Federation((const Federation &)result); 
  return jresult;
}


SWIGEXPORT jlong JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1andOp_1_1SWIG_10(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_) {
  jlong jresult = 0 ;
  Federation *arg1 = (Federation *) 0 ;
  Federation *arg2 = 0 ;
  SwigValueWrapper< Federation > result;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  (void)jarg2_;
  arg1 = *(Federation **)&jarg1; 
  arg2 = *(Federation **)&jarg2;
  if (!arg2) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "Federation & reference is null");
    return 0;
  } 
  result = (arg1)->andOp(*arg2);
  *(Federation **)&jresult = new Federation((const Federation &)result); 
  return jresult;
}


SWIGEXPORT jlong JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1andOp_1_1SWIG_11(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_) {
  jlong jresult = 0 ;
  Federation *arg1 = (Federation *) 0 ;
  AtomConstraint *arg2 = 0 ;
  SwigValueWrapper< Federation > result;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  (void)jarg2_;
  arg1 = *(Federation **)&jarg1; 
  arg2 = *(AtomConstraint **)&jarg2;
  if (!arg2) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "AtomConstraint const & reference is null");
    return 0;
  } 
  result = (arg1)->andOp((AtomConstraint const &)*arg2);
  *(Federation **)&jresult = new Federation((const Federation &)result); 
  return jresult;
}


SWIGEXPORT jlong JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1addOp(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_) {
  jlong jresult = 0 ;
  Federation *arg1 = (Federation *) 0 ;
  Federation *arg2 = 0 ;
  SwigValueWrapper< Federation > result;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  (void)jarg2_;
  arg1 = *(Federation **)&jarg1; 
  arg2 = *(Federation **)&jarg2;
  if (!arg2) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "Federation & reference is null");
    return 0;
  } 
  result = (arg1)->addOp(*arg2);
  *(Federation **)&jresult = new Federation((const Federation &)result); 
  return jresult;
}


SWIGEXPORT jlong JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1minusOp(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_) {
  jlong jresult = 0 ;
  Federation *arg1 = (Federation *) 0 ;
  Federation *arg2 = 0 ;
  SwigValueWrapper< Federation > result;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  (void)jarg2_;
  arg1 = *(Federation **)&jarg1; 
  arg2 = *(Federation **)&jarg2;
  if (!arg2) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "Federation & reference is null");
    return 0;
  } 
  result = (arg1)->minusOp(*arg2);
  *(Federation **)&jresult = new Federation((const Federation &)result); 
  return jresult;
}


SWIGEXPORT void JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1up(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_) {
  Federation *arg1 = (Federation *) 0 ;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  arg1 = *(Federation **)&jarg1; 
  (arg1)->up();
}


SWIGEXPORT void JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1down(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_) {
  Federation *arg1 = (Federation *) 0 ;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  arg1 = *(Federation **)&jarg1; 
  (arg1)->down();
}


SWIGEXPORT void JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1mergeReduce(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jint jarg2, jint jarg3) {
  Federation *arg1 = (Federation *) 0 ;
  int arg2 ;
  int arg3 ;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  arg1 = *(Federation **)&jarg1; 
  arg2 = (int)jarg2; 
  arg3 = (int)jarg3; 
  (arg1)->mergeReduce(arg2,arg3);
}


SWIGEXPORT void JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1freeClock(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jint jarg2) {
  Federation *arg1 = (Federation *) 0 ;
  int arg2 ;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  arg1 = *(Federation **)&jarg1; 
  arg2 = (int)jarg2; 
  (arg1)->freeClock(arg2);
}


SWIGEXPORT jboolean JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1lt(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_) {
  jboolean jresult = 0 ;
  Federation *arg1 = (Federation *) 0 ;
  Federation *arg2 = 0 ;
  bool result;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  (void)jarg2_;
  arg1 = *(Federation **)&jarg1; 
  arg2 = *(Federation **)&jarg2;
  if (!arg2) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "Federation & reference is null");
    return 0;
  } 
  result = (bool)(arg1)->lt(*arg2);
  jresult = (jboolean)result; 
  return jresult;
}


SWIGEXPORT jboolean JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1gt(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_) {
  jboolean jresult = 0 ;
  Federation *arg1 = (Federation *) 0 ;
  Federation *arg2 = 0 ;
  bool result;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  (void)jarg2_;
  arg1 = *(Federation **)&jarg1; 
  arg2 = *(Federation **)&jarg2;
  if (!arg2) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "Federation & reference is null");
    return 0;
  } 
  result = (bool)(arg1)->gt(*arg2);
  jresult = (jboolean)result; 
  return jresult;
}


SWIGEXPORT jboolean JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1le(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_) {
  jboolean jresult = 0 ;
  Federation *arg1 = (Federation *) 0 ;
  Federation *arg2 = 0 ;
  bool result;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  (void)jarg2_;
  arg1 = *(Federation **)&jarg1; 
  arg2 = *(Federation **)&jarg2;
  if (!arg2) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "Federation & reference is null");
    return 0;
  } 
  result = (bool)(arg1)->le(*arg2);
  jresult = (jboolean)result; 
  return jresult;
}


SWIGEXPORT jboolean JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1ge(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_) {
  jboolean jresult = 0 ;
  Federation *arg1 = (Federation *) 0 ;
  Federation *arg2 = 0 ;
  bool result;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  (void)jarg2_;
  arg1 = *(Federation **)&jarg1; 
  arg2 = *(Federation **)&jarg2;
  if (!arg2) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "Federation & reference is null");
    return 0;
  } 
  result = (bool)(arg1)->ge(*arg2);
  jresult = (jboolean)result; 
  return jresult;
}


SWIGEXPORT jboolean JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1eq(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_) {
  jboolean jresult = 0 ;
  Federation *arg1 = (Federation *) 0 ;
  Federation *arg2 = 0 ;
  bool result;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  (void)jarg2_;
  arg1 = *(Federation **)&jarg1; 
  arg2 = *(Federation **)&jarg2;
  if (!arg2) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "Federation & reference is null");
    return 0;
  } 
  result = (bool)(arg1)->eq(*arg2);
  jresult = (jboolean)result; 
  return jresult;
}


SWIGEXPORT void JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1setZero(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_) {
  Federation *arg1 = (Federation *) 0 ;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  arg1 = *(Federation **)&jarg1; 
  (arg1)->setZero();
}


SWIGEXPORT void JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1predt(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_) {
  Federation *arg1 = (Federation *) 0 ;
  Federation *arg2 = 0 ;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  (void)jarg2_;
  arg1 = *(Federation **)&jarg1; 
  arg2 = *(Federation **)&jarg2;
  if (!arg2) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "Federation & reference is null");
    return ;
  } 
  (arg1)->predt(*arg2);
}


SWIGEXPORT void JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1intern(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_) {
  Federation *arg1 = (Federation *) 0 ;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  arg1 = *(Federation **)&jarg1; 
  (arg1)->intern();
}


SWIGEXPORT void JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1setInit(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_) {
  Federation *arg1 = (Federation *) 0 ;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  arg1 = *(Federation **)&jarg1; 
  (arg1)->setInit();
}


SWIGEXPORT void JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1convexHull(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_) {
  Federation *arg1 = (Federation *) 0 ;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  arg1 = *(Federation **)&jarg1; 
  (arg1)->convexHull();
}


SWIGEXPORT jboolean JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1containsIntValuation(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_) {
  jboolean jresult = 0 ;
  Federation *arg1 = (Federation *) 0 ;
  SwigValueWrapper< IntClockValuation > arg2 ;
  IntClockValuation *argp2 ;
  bool result;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  (void)jarg2_;
  arg1 = *(Federation **)&jarg1; 
  argp2 = *(IntClockValuation **)&jarg2; 
  if (!argp2) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "Attempt to dereference null IntClockValuation");
    return 0;
  }
  arg2 = *argp2; 
  result = (bool)(arg1)->containsIntValuation(arg2);
  jresult = (jboolean)result; 
  return jresult;
}


SWIGEXPORT jboolean JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1containsDoubleValuation(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_) {
  jboolean jresult = 0 ;
  Federation *arg1 = (Federation *) 0 ;
  SwigValueWrapper< DoubleClockValuation > arg2 ;
  DoubleClockValuation *argp2 ;
  bool result;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  (void)jarg2_;
  arg1 = *(Federation **)&jarg1; 
  argp2 = *(DoubleClockValuation **)&jarg2; 
  if (!argp2) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "Attempt to dereference null DoubleClockValuation");
    return 0;
  }
  arg2 = *argp2; 
  result = (bool)(arg1)->containsDoubleValuation(arg2);
  jresult = (jboolean)result; 
  return jresult;
}


SWIGEXPORT void JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1myExtrapolateMaxBounds(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_) {
  Federation *arg1 = (Federation *) 0 ;
  SwigValueWrapper< IntVector > arg2 ;
  IntVector *argp2 ;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  (void)jarg2_;
  arg1 = *(Federation **)&jarg1; 
  argp2 = *(IntVector **)&jarg2; 
  if (!argp2) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "Attempt to dereference null IntVector");
    return ;
  }
  arg2 = *argp2; 
  (arg1)->myExtrapolateMaxBounds(arg2);
}


SWIGEXPORT jboolean JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1hasZero(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_) {
  jboolean jresult = 0 ;
  Federation *arg1 = (Federation *) 0 ;
  bool result;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  arg1 = *(Federation **)&jarg1; 
  result = (bool)(arg1)->hasZero();
  jresult = (jboolean)result; 
  return jresult;
}


SWIGEXPORT void JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1updateValue_1_1SWIG_10(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jint jarg2, jint jarg3) {
  Federation *arg1 = (Federation *) 0 ;
  int arg2 ;
  int arg3 ;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  arg1 = *(Federation **)&jarg1; 
  arg2 = (int)jarg2; 
  arg3 = (int)jarg3; 
  (arg1)->updateValue(arg2,arg3);
}


SWIGEXPORT jint JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1size(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_) {
  jint jresult = 0 ;
  Federation *arg1 = (Federation *) 0 ;
  int result;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  arg1 = *(Federation **)&jarg1; 
  result = (int)(arg1)->size();
  jresult = (jint)result; 
  return jresult;
}


SWIGEXPORT jint JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1hash(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_) {
  jint jresult = 0 ;
  Federation *arg1 = (Federation *) 0 ;
  int result;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  arg1 = *(Federation **)&jarg1; 
  result = (int)(arg1)->hash();
  jresult = (jint)result; 
  return jresult;
}


SWIGEXPORT jboolean JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1isEmpty(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_) {
  jboolean jresult = 0 ;
  Federation *arg1 = (Federation *) 0 ;
  bool result;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  arg1 = *(Federation **)&jarg1; 
  result = (bool)(arg1)->isEmpty();
  jresult = (jboolean)result; 
  return jresult;
}


SWIGEXPORT void JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1updateValue_1_1SWIG_11(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jint jarg3) {
  Federation *arg1 = (Federation *) 0 ;
  cindex_t arg2 ;
  int32_t arg3 ;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  arg1 = *(Federation **)&jarg1; 
  arg2 = (cindex_t)jarg2; 
  arg3 = (int32_t)jarg3; 
  (arg1)->updateValue(arg2,arg3);
}


SWIGEXPORT void JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1updateClock(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jlong jarg3) {
  Federation *arg1 = (Federation *) 0 ;
  cindex_t arg2 ;
  cindex_t arg3 ;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  arg1 = *(Federation **)&jarg1; 
  arg2 = (cindex_t)jarg2; 
  arg3 = (cindex_t)jarg3; 
  (arg1)->updateClock(arg2,arg3);
}


SWIGEXPORT void JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1updateIncrement(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jint jarg3) {
  Federation *arg1 = (Federation *) 0 ;
  cindex_t arg2 ;
  int32_t arg3 ;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  arg1 = *(Federation **)&jarg1; 
  arg2 = (cindex_t)jarg2; 
  arg3 = (int32_t)jarg3; 
  (arg1)->updateIncrement(arg2,arg3);
}


SWIGEXPORT void JNICALL Java_epmc_udbm_udbm_1intJNI_Federation_1update(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jlong jarg3, jint jarg4) {
  Federation *arg1 = (Federation *) 0 ;
  cindex_t arg2 ;
  cindex_t arg3 ;
  int32_t arg4 ;
  
  (void)jenv;
  (void)jcls;
  (void)jarg1_;
  arg1 = *(Federation **)&jarg1; 
  arg2 = (cindex_t)jarg2; 
  arg3 = (cindex_t)jarg3; 
  arg4 = (int32_t)jarg4; 
  (arg1)->update(arg2,arg3,arg4);
}


SWIGEXPORT void JNICALL Java_epmc_udbm_udbm_1intJNI_delete_1Federation(JNIEnv *jenv, jclass jcls, jlong jarg1) {
  Federation *arg1 = (Federation *) 0 ;
  
  (void)jenv;
  (void)jcls;
  arg1 = *(Federation **)&jarg1; 
  delete arg1;
}


#ifdef __cplusplus
}
#endif

