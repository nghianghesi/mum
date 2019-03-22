#include <stdio.h>
#include <omp.h>
#include <iostream>
#include <chrono>
#include <string>

#include <cstdlib> 
#include <ctime> 
#include <thread>

using namespace std;
using namespace std::chrono;

#define n 700
int a[n][n], b[n][n], c[n][n];
int i, j, k;
clock_t t;
int 	max_thread_capacity = std::thread::hardware_concurrency();

void initResult() {
	for (i = 0; i < n; i++) {
		for (j = 0; j < n; j++) {
			c[i][j] = 0;
		}
	}
}

void loopj1nk1n() {
	initResult();

	t = clock();
#pragma omp parallel for  private(j,k)
	for (i = 0; i < n; i++) {
		for (j = 0; j < n; j++) {
			for (k = 0; k < n; k++) {
				c[i][j] += a[i][k] * b[k][j];
			}
		}
	}

	printf("time j 1->n k 1->n %d \n", (clock() - t));

}

void loopjn1k1n() {
	initResult();

	t = clock();	
#pragma omp parallel for private(j,k)
	for (i = 0; i < n; i++) {
		for (j = n-1; j >= 0; j--) {
			for (k = 0; k < n; k++) {
				c[i][j] += a[i][k] * b[k][j];
			}
		}
	}	
	printf("time j n->1 k 1->n %d \n", (clock() - t));

}

void loopjn1kn1() {
	initResult();

	t = clock();
#pragma omp parallel for  private(j,k)
	for (i = 0; i < n; i++) {
		for (j = n - 1; j >= 0; j--) {
			for (k = n-1; k >= 0; k--) {
				c[i][j] += a[i][k] * b[k][j];
			}
		}
	}	
	printf("time j n->1 k n->1 %d \n", (clock() - t));

}

///////

void loopk1nj1n() {
	initResult();

	t = clock();
#pragma omp parallel for  private(j,k)
	for (i = 0; i < n; i++) {
		for (k = 0; k < n; k++) {
			for (j = 0; j < n; j++) {
				c[i][j] += a[i][k] * b[k][j];
			}
		}
	}

	printf("time k 1->n  j 1->n %d \n", (clock() - t));

}

void loopk1njn1() {
	initResult();

	t = clock();
#pragma omp parallel for private(j,k)
	for (i = 0; i < n; i++) {
			for (k = 0; k < n; k++) {
				for (j = n - 1; j >= 0; j--) {
				c[i][j] += a[i][k] * b[k][j];
			}
		}
	}
	printf("time k 1->n j n->1 %d \n", (clock() - t));

}

void loopkn1jn1() {
	initResult();

	t = clock();
#pragma omp parallel for  private(j,k)
	for (i = 0; i < n; i++) {
			for (k = n - 1; k >= 0; k--) {
				for (j = n - 1; j >= 0; j--) {

				c[i][j] += a[i][k] * b[k][j];
			}
		}
	}
	printf("time k n->1 j n->1 %d \n", (clock() - t));

}


void main() {
	omp_set_num_threads(max_thread_capacity);
#pragma parallel for private(j,k)
	for (i = 0; i < n; i++) {
		for (j = 0; j < n; j++) {
			a[i][j] = rand()%1000;
			b[i][j] = rand()%1000;
		}
	}
	loopj1nk1n();
	loopjn1k1n();
	loopjn1kn1();
	loopk1nj1n();
	loopk1njn1();
	loopkn1jn1();
	std::string str;
	std::getline(std::cin, str);
}
