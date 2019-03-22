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
#include <iostream>
#include <thread>

#include <stdio.h>
#include <iostream>

using namespace std;
using namespace std::chrono;


#define n 100000000 /*length of list*/
#define bsize 1000000 /*size of buckets*/
#define m 100 /*number of buckets*/
int *list = (int*)calloc(n, sizeof(int)); /*unsorted list of integers*/
int *final = (int*)calloc(n, sizeof(int)); /*sorted list of integers*/
int bucket[m][bsize]; /*buckets*/
int bucketcount[m]; /*number of items stored in bucket*/
int bucketstart[m]; 
int maxvalue, minvalue;
int buket_range;
int i, j, k;
int max_thread_capacity, tidx;
int dynamictrunk = 1;

clock_t ts = clock();
clock_t t = clock();
int sequentialTime, parallelTime;

void InitTestValues() {
	omp_set_num_threads(max_thread_capacity);
	#pragma omp parallel for 
	for (i = 0; i < n; i++) {
		list[i] = (rand() +1 ) * (rand()+1);
		final[i] = 0;
	}
}

void InitRun() {

	omp_set_num_threads(max_thread_capacity);
	#pragma omp parallel for 
	for (j = 0; j < m; j++) {
		bucketcount[j] = 0;
		bucketstart[j] = 0;
	}
	maxvalue = 0;
	minvalue = 0;

	ts = t = clock();
}

int lt(const void *p, const void *q) {
	return (*(int *)p - *(int *)q);
}void verifyDistribute() {/*	int totalcheck = 0;
	for (j = 0; j < m; j++) {
		totalcheck += bucketcount[j];
	}
	assert(totalcheck == n);*/}void verifySort() {	//for (i = 0; i < n - 1; i++) {
	//	assert(final[i] <= final[i + 1]);
	//}
}
void distribute(){

	//find minvalue;
	maxvalue = list[0];
	minvalue = list[0];

	for (i = 0; i < n; i++) {
		minvalue = min(minvalue, list[i]);
	}

	for (i = 0; i < n; i++) {
		maxvalue = max(maxvalue, list[i]);
	}
	buket_range = (int)(1.0*((maxvalue - minvalue + 1) / m) + 1);

	// split bucket;
	for (i = 0; i < n; i++) {
		j = (list[i] - minvalue) / buket_range;
		bucket[j][bucketcount[j]] = list[i];
		bucketcount[j] += 1;
	}

	printf("distribution %d / ", (clock() - t));
	t = clock();

	verifyDistribute();
}

void sequentialSort() {
	// quick sort buckets
	for (j = 0; j < m; j++) {
		qsort(bucket[j], bucketcount[j], sizeof(int), lt);
	}
	printf("sequential qsort %d / ", (clock() - t));
	t = clock();
}

void staticParallelSort() {
	// quick sort buckets

	omp_set_num_threads(max_thread_capacity);
	#pragma omp parallel for 
	for (j = 0; j < m; j++) {
		qsort(bucket[j], bucketcount[j], sizeof(int), lt);
	}
	printf("static parallel qsort %d / ", (clock() - t));
	t = clock();
}

void dynamicParallelSort() {
	// quick sort buckets
	omp_set_num_threads(max_thread_capacity);
	#pragma omp parallel for schedule(dynamic,dynamictrunk)
	for (j = 0; j < m; j++) {
		qsort(bucket[j], bucketcount[j], sizeof(int), lt);
	}
	printf("dynamic parallel qsort %d / ", (clock() - t));
	t = clock();
}

void sequentialMerge() {
	i = 0;
	for (j = 0; j < m; j++) {
		for (k = 0; k < bucketcount[j]; k++,i++) {
			final[i] = bucket[j][k];
		}
	}	
	
	sequentialTime = clock() - ts;
	printf("Sequential merge phrase %d / ", (clock() - t));
	printf("total %d \n", sequentialTime);
	verifySort();
}

void staticParallelMerge(){

	//merge final result
	for (j = 1; j < m; j++) {
		bucketstart[j] = bucketstart[j - 1] + bucketcount[j - 1];
	}

	omp_set_num_threads(max_thread_capacity);
	#pragma omp parallel for private(k)
	for (j = 0; j < m; j++) {
		for (k = 0; k < bucketcount[j]; k++) {
			final[bucketstart[j] + k] = bucket[j][k];
		}
	}

	parallelTime = clock() - ts;
	printf("static merge phrase %d / ", (clock() - t));
	printf("total %d, speed-up %f\n", parallelTime, 1.0*sequentialTime/parallelTime);
	verifySort();
}


void dynamicParallelMerge() {

	//merge final result
	for (j = 1; j < m; j++) {
		bucketstart[j] = bucketstart[j - 1] + bucketcount[j - 1];
	}

	omp_set_num_threads(max_thread_capacity);
	#pragma omp parallel for private(k) schedule(dynamic,dynamictrunk)
	for (j = 0; j < m; j++) {
		for (k = 0; k < bucketcount[j]; k++) {
			final[bucketstart[j] + k] = bucket[j][k];
		}
	}
	parallelTime = clock() - ts;
	printf("dynamic merge phrase %d / ", (clock() - t));
	printf("total %d, speed-up %f\n", parallelTime, 1.0*sequentialTime / parallelTime);
	verifySort();
}

void main() {

	printf("number of items %d\n", n);
	max_thread_capacity = std::thread::hardware_concurrency();
	InitTestValues();

	InitRun();
		distribute();
		sequentialSort();
		sequentialMerge();

		InitRun();
		distribute();
		staticParallelSort();
		staticParallelMerge();

		InitRun();
		distribute();
		dynamicParallelSort();
		dynamicParallelMerge();

	std::string str;
	std::getline(std::cin, str);

}
