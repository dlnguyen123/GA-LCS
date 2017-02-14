/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcs;

import java.util.Random;

/**
 *
 * @author Reuben
 * 
 * Sub-sequencer takes two strings and return the largest common sub-sequence
 * of the two strings.
 * 
 * M = population size
 * G = Max # of generations (or state of termination)
 * pL = probability of crossover
 * pM = probability of mutation
 * pR = probability of reproduction
 */
public class LCS_API {
    private int[][] parents;
    private int[][] children;
    private double[] fitness;
    private String shorterString, longerString;
   
    //private int populationSize;         //M
    //private int maxNumberOfGenerations; //G
    //private double probOfXover;         //pL
    //private double probOfMutation;      //pM
    //private double probOfReproduction;  //pR
    
    public String findLcs(String firstInput, String secondInput, int M, int G, double pL, double pM, double pR)
    {
        //initializes the result string
        String result="";
        
        //sets the variable to the string that is smaller or equeal to
        shorterString = ((firstInput.length()<=secondInput.length())? firstInput : secondInput );
        //sets the comparer variable to the string that is larger
        longerString = ((firstInput.length()>secondInput.length())? firstInput : secondInput );
        
        init(M,shorterString.length());
        
        do{
            breedParents(M, pL, pM, pR);
            
            //G is the max number of generations
            //It also checks whether the values have converged
            G--;
        }while(G > 0 & !isConverged(M));
        
        //build up result string of best solution
        int bestSolutionIndex = getBestSolution();
        for (int i = 0; i < parents[bestSolutionIndex].length; i++) {
            if(parents[bestSolutionIndex][i]==1)
            {
                result += shorterString.charAt(i);
            }
        }
        
        return result;
    }
    
    private void init(int populationSize, int resultSize)
    {
        //set the size of the parent and children arrays
        parents = new int[populationSize][resultSize];
        children = new int[populationSize][resultSize];
        fitness = new double[populationSize];
        Random rnd = new Random();
        
        for(int i = 0; i<populationSize; i++)
        {
            for(int j = 0; j<resultSize; j++)
            {
                parents[i][j] = rnd.nextInt(2);
            }
        }
    }
    
    private boolean isConverged(int populationSize)
    {
        boolean result = true;
        for (int i = 1; i < populationSize; i++) {
            if (parents[0] != parents[i])
            {
                result = false;
            }
        }
        return result;
    }
    
    /**
     * checkFitness
     * @param solution
     * @return double value that represents the solution's fitness
     * 
     * Algorithm:
     * 1. For each character in the shorter string:
     * 2. Check if current character is "selected":
     * 3. If not selected, go to step 2 and next character.
     * 4. If selected, check if the character is in the longer string from the
     *    base position to the end. Add a value to the fitness.
     * 5. If it is, set that position as the new base position and add to 
     *    the overall fitness of the solution.
     * 6. If it is not found, keep the same base position as before and reduce
     *    the overall fitness of the solution.
     * 7. Repeat steps 2 through 6 until the solution is checked and return the
     *    fitness value that is found.
     */
    private double checkFitness(int[] solution) {
        //Uses the shorter and longer strings
        char currentChar;
        int basePosition = 0;
        int currentPosition;
        boolean success = false;
        double myFitness = 0.0;
        
        //for each selected letter, test it against the longer string from the
        //current position to the last.
        for (int i = 0; i < solution.length; i++) 
        {
            //If the current position is a "1" (selected)
            if (solution[i] == 1)
            {
                //Add to the fitness
                myFitness += .25;
                
                //Set currentChar to the current character of the solution
                currentChar = shorterString.charAt(i);

                //Reset the current position for each letter in the solution
                currentPosition = 0;

                do //for every character in the longer string
                { 
                    //if the current character is found in the longer string
                    if (currentChar == longerString.charAt(basePosition+currentPosition)) 
                    {
                        //Mark it as a success
                        success = true;
                    }

                    //increment the position
                    currentPosition++;
                } while (!success & (basePosition+currentPosition) < longerString.length());

                //if the previous loop found the current character in the longer string
                if (success) {
                    //Increase the fitness
                    myFitness += .5;
                    //Set the new base position to the relative current position
                    basePosition += currentPosition;
                }
                else{
                    //decrease the fitness
                    myFitness -= .5;
                }
            }
            else
            {
                //If the position is not selected, reduce fitness by a little
                myFitness -= .125;
            }
        }
        
        return myFitness;
    }
    
    private int[] xoverSolution(int[] firstSolution, int[] secondSolution) 
    {
        //Takes two possible solution int arrays and picks a random point
        //to crossover the values and returns two children sets.
      
        //Could potentially have it repeat a set number of times depending on probability?
        int[]child = new int[firstSolution.length];
        int xover = (int) (Math.random()*firstSolution.length); //make crossover
        for (int i = 0;i<firstSolution.length;i++)
        {
            if (i < xover)
                child[i]=firstSolution[i];
            else
                child[i]=secondSolution[i];
            
        }
        return child;
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int[] mutateSolution(int[] parent) 
    {
        //For a given solution, pick a random point and flip it.
        //Could repeat this a random number of times, repeating the 
        //probability check every time.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private double[] normalizeFitness(double [] oldFitness)
    {
        double[] rankedFitness = new double[oldFitness.length];
        double currentSmallest;
        int smallestIndex = 0;
        
        //for every fitness in the array
        for (int i = 0; i < oldFitness.length; i++) {
            //find the ith smallest, add it to the new array, and set the old to
            //a very large value to avoid being picked again
            
            currentSmallest = Double.MAX_VALUE;
            
            //find smallest value
            for (int j = 0; j < oldFitness.length; j++) 
            {
                if (currentSmallest>=oldFitness[j])
                {
                    currentSmallest = oldFitness[j];
                    smallestIndex = j;
                }
            }
            
            //add smallest value to new array
            rankedFitness[smallestIndex] = i + 1;
            oldFitness[smallestIndex] = Double.MAX_VALUE;
        }
        return rankedFitness;
    }
    
    /**
     * breedParents
     * @param populationSize
     * @param pL
     * @param pM
     * @param pR 
     * Algorithm:
     * 
     */
    private void breedParents(int populationSize, double pL, double pM, double pR) 
    {
        //get fitness of all parents
        for (int i = 0; i < populationSize; i++) {
            //for each solution, check its fitness
            fitness[i] = checkFitness(parents[i]);
        }
        
        //Rank and readjust the fitness of the parents for better results
        fitness = normalizeFitness(fitness);
        
        //Repeat the breeding process to produce every child
        for (int j = 0; j < populationSize; j++) {
            //pick parents to combine via roullette wheel and fitness
            int firstParent = rouletteSelect(fitness);
            //pick second parent that is unique
            int secondParent;

            //Combine using one of the methods via the probablility
            switch(rouletteSelect(new double[]{pL, pM, pR}))
            {
                case 1:
                    //crossover
                    //pick second parent that is unique to breed 
                    secondParent = rouletteSelect(fitness);
                    //Gets and adds the two (or multiple) children
                    //int[][] multipleChildren = xoverSoution(parents[firstParent], parents[secondParent]);
                    //for (int[] multipleChildren1 : multipleChildren) {
                    //    children[j] = multipleChildren1;
                    //    j++;
                    //}
                    break;
                case 2:
                    //mutation - mutate the parent values
                    //children[j]=mutateSolution(parents[firstParent]);
                    break;
                case 3:
                    //reproduction
                    children[j]=parents[firstParent];
                    break;
                default:
                    //Default? Probably just set it to replacement
                    children[j]=parents[firstParent];//reproduction
                    break;
            }     
        }
        
        //Make children the parents
        children = parents;
    }
    
    private int getBestSolution()
    {
        int largestIndex = 0;
        double currentLargestFitness = Double.MIN_VALUE;
        
        //get fitness of all parents
        for (int i = 0; i < parents.length; i++) {
            //for each solution, check its fitness
            fitness[i] = checkFitness(parents[i]);
            
            if (fitness[i] > currentLargestFitness)
            {
                largestIndex = i;
            }
        }
        
        return largestIndex;
    }
    
    /**
     * The following roulette wheel code is adapted from
     * the Wikipedia example code at the following link:
     * https://en.wikipedia.org/wiki/Fitness_proportionate_selection
     */
    // Returns the selected index based on the weights(probabilities)
    private int rouletteSelect(double[] weight) {
            // calculate the total weight
            double weight_sum = 0;
            for(int i=0; i<weight.length; i++) {
                    weight_sum += weight[i];
            }
            // get a random value
            double value = randUniformPositive() * weight_sum;	
            // locate the random value based on the weights
            for(int i=0; i<weight.length; i++) {		
                    value -= weight[i];		
                    if(value <= 0) return i;
            }
            // when rounding errors occur, we return the last item's index 
            return weight.length - 1;
    }

    // Returns a uniformly distributed double value between 0.0 and 1.0
    private double randUniformPositive() {
            // easiest implementation
            return new Random().nextDouble();
    }
}
