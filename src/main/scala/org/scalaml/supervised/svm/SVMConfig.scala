/**
 * Copyright (c) 2013-2015  Patrick Nicolas - Scala for Machine Learning - All rights reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License") you may not use this file 
 * except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * The source code in this file is provided by the author for the sole purpose of illustrating the 
 * concepts and algorithms presented in "Scala for Machine Learning". 
 * ISBN: 978-1-783355-874-2 Packt Publishing.
 * 
 * Version 0.99.1
 */
package org.scalaml.supervised.svm

import libsvm._
import org.scalaml.core.Design.Config
import org.scalaml.supervised.svm.kernel.SVMKernel
import org.scalaml.supervised.svm.formulation.SVMFormulation


		/**
		 * Generic configuration item for support vector machine.
		 * 
		 * @author Patrick Nicolas
		 * @since April 28, 2014
		 * @note Scala for Machine Learning Chapter 8 Kernel models and support vector machines
		 */
private[scalaml] trait SVMConfigItem {
		/**
		 * Update the LIBSVM configuration parameter.
		 * @param param LIBSVM parameter to update.
		 */
	 def update(param: svm_parameter): Unit
}



		/**
		 * Generic configuration manager for any category of SVM algorithm. The configuration of 
		 * a SVM has three elements: SVM formulation, Kernel function and the execution parameters.
		 * @constructor Create a configuration for this SVM with a given formulation, kernel function 
		 * and execution parameters.
		 * @see LIBSVM
		 * @param formulation Formulation of the SVM problem (type and parameters of the formulation 
		 * of the SVM algorithm)
		 * @param kernel Kernel function used for non-separable training sets (type and parameter(s) 
		 * of the Kernel function used for non-linear problems
		 * @param exec Execution parameters for the training of the SVM model.
		 * 
		 * @author Patrick Nicolas
		 * @since 0.98 April 30, 2014
		 * @see Scala for Machine Learning Chapter 8 Kernel models and support vector machines.
		 */
final protected class SVMConfig(
		formulation: SVMFormulation, 
		kernel: SVMKernel, 
		exec: SVMExecution) extends Config {
	import SVMConfig._
	  
		/**
		 * Configuration parameters set used in LIBSVM
		 */
	var param = new svm_parameter
	formulation.update(param)
	kernel.update(param)
	exec.update(param)
    	
	override def toString: String = 
			s"\nSVM Formulation: ${formulation.toString}\n${kernel.toString}"

		/**
		 * Retrieve the convergence criteria of SVMExecution class
		 * @return convergence criteria
		 */
	@inline 
	final def eps: Double = exec.eps
    
		/**
		 * Test if SVM is configured for cross validation
		 * @return true if number of folds > 0, false otherwise
		 */
	@inline 
	final def isCrossValidation: Boolean = exec.nFolds > 0

		/**
		 * Retrieve the number of folds used in the cross-validation
		 * @return Number of folds used in the cross-validation
		 */
	@inline 
	final def nFolds: Int = exec.nFolds
}



		/**
		 * Companion object for SVM configuration manager used for defining the constructors of 
		 * SVMConfig class.
		 * @author Patrick Nicolas
		 * @since April 30, 2014
		 * @note Scala for Machine Learning Chapter 8 Kernel models and support vector machines.
		 */
object SVMConfig {
	import SVMExecution._
	
		/**
		 * Default constructor for the configuration of the support vector machine
		 * @param svmType Formulation of the SVM problem (type and parameters of the formulation
		 * of the SVM algorithm)
		 * @param kernel Kernel function used for non-separable training sets (type and parameter(s) 
		 * of the Kernel function used for non-linear problems
		 * @param svmParams Execution parameters for the training of the SVM model.
		 */
	def apply(svmType: SVMFormulation, kernel: SVMKernel, svmParams: SVMExecution): SVMConfig = 
		new SVMConfig(svmType, kernel, svmParams)

		/**
		 * Constructor for the configuration of the support vector machine with a predefined execution 
		 * parameters
		 * @param svmType Formulation of the SVM problem (type and parameters of the formulation
		 * of the SVM algorithm)
		 * @param kernel Kernel function used for non-separable training sets (type and parameter(s) 
		 * of the Kernel function used for non-linear problems
		 */
	def apply(svmType: SVMFormulation, kernel: SVMKernel): SVMConfig = 
		new SVMConfig(svmType, kernel, SVMExecution.apply)
}


// --------------------------- EOF ------------------------------------------