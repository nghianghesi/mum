#include <stdio.h>
#include <omp.h>
#include <iostream>
#include <chrono>
#include <string>

#include <cstdlib> 
#include <ctime> 
#include <thread>
#include <windows.h>

using namespace std;
using namespace std::chrono;
double sum;
double t,ft;
double w;
int sequentialtime, paralleltime;

#define n 100000000
#define a 0
#define b 2
int i;
clock_t ttime;
int max_thread_capacity = std::thread::hardware_concurrency();

double f(double x) {
	return sqrt(4.0 - x*x);
}

void sequential() {
	ttime = clock();
	sum = 0.0;
	w = 1.0 * (b - a) / n;
	t = a;
	for (i = 1; i < n; i++) {
		t += w;
		sum = sum + f(t); /*Add point to sum*/
	}
	sum = sum + (f(a) + f(b)) / 2;
	sum = w * sum;
	sequentialtime = clock() - ttime;
	printf("sequential: result %f, time %d \n", sum, sequentialtime);
}

void parallel() {
	ttime = clock();
	sum = 0.0;
	w = 1.0 * (b - a) / n;
	omp_set_num_threads(max_thread_capacity);
	#pragma omp parallel reduction(+:sum)
	{
		#pragma omp for
		for (i = 1; i < n; i++) {
			sum = sum + f(a + w*i); /*Add point to sum*/
		}
	}
	sum = sum + (f(a) + f(b)) / 2;
	sum = w * sum;
	paralleltime = (clock() - ttime);
	printf("parallel - reduction: result %f, time %d, speed up %f\n", sum, paralleltime, 1.0*sequentialtime/paralleltime);
}


void parallelCritical() {
	ttime = clock();
	sum = 0.0;
	w = 1.0 * (b - a) / n;
	omp_set_num_threads(max_thread_capacity);
#pragma omp parallel private (ft)
	{
#pragma omp for
		for (i = 1; i < n; i++) {
			ft = f(a + w*i);
#pragma omp critical
			sum = sum + ft; /*Add point to sum*/
		}
	}
	sum = sum + (f(a) + f(b)) / 2;
	sum = w * sum;
	paralleltime = (clock() - ttime);
	printf("parallel critical: result %f, time %d, speed up %f\n", sum, paralleltime, 1.0*sequentialtime / paralleltime);
}


void parallelNoPragmaFor() {
	ttime = clock();
	sum = 0.0;
	w = 1.0 * (b - a) / n;

	int steps = n / max_thread_capacity;
	omp_set_num_threads(max_thread_capacity);
	#pragma omp parallel private (i, t) reduction(+:sum)
	{
		for (i = 0, t = a + w*omp_get_thread_num() * steps; i < steps; i++, t+=w) {
			sum += f(t); /*Add point to sum*/
		}
#pragma omp sections
		{
#pragma omp section
			{
				sum += (f(a) + f(b)) / 2;
			}
		}
	}
	sum = w * sum;
	paralleltime = (clock() - ttime);
	printf("parallel reduction: result %f, time %d, speed up %f\n", sum, paralleltime, 1.0*sequentialtime / paralleltime);
}


void main() {
	sequential();
	//parallelCritical();
	parallelNoPragmaFor();
	std::string str;
	std::getline(std::cin, str);
}