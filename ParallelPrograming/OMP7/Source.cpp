#define n 50
#include <stdio.h>
#include <iostream>
#include <chrono>
#include <string>
#include <assert.h>     /* assert */

#include <cstdlib> 
#include <ctime> 
#include <algorithm> 
#include <omp.h>
#include <cmath>
#include <chrono>
#include <thread>
using namespace std;    //for using cout


int i, tid;
void main() {
	/*omp_set_num_threads(3);
	#pragma omp parallel for private(tid) schedule(static,1)
	for (i = 0; i < n; i++) {
		tid = omp_get_thread_num();
		printf("Thread %d executing iteration %d\n", tid, i);
	}*/

	omp_set_num_threads(3);

#pragma omp parallel for private(tid) schedule(guided,10)
	for (i = 0; i < n; i++) {
		std::this_thread::sleep_for(chrono::milliseconds(10+rand()%10));


		tid = omp_get_thread_num();

		printf("Thread %d executing iteration %d\n", tid, i);
		//mySleep(50 + rand() % 100);
	}
	
	std::string str;
	std::getline(std::cin, str);}