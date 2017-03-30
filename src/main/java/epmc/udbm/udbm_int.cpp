#include "udbm_int.h"
using namespace dbm;
using namespace std;


void VarNamesAccessor::setClockName(int index, std::string name) {
    names_map[index] = string(name);
}

// const std::string VarNamesAccessor::getClockName(int i) const {
//     map<int, string>::const_iterator iterator = this->names_map.find(i);
//     return iterator->second;
// }

bool VarNamesAccessor::hasClockName(int i) const {
    map<int, string>::const_iterator iterator = this->names_map.find(i);
    return (iterator != names_map.end());
}

 // double My_variable = 3.0;
 
 // int fact(int n) {
 //     if (n <= 1) return 1;
 //     else return n*fact(n-1);
 // }
 // 