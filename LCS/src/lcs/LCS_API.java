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
            //if we decide to go with an alternate convergence mecahnism
            G--;
        }while(G > 0);
        
        
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
    
    private double checkFitness(int[] solution) {
        //Uses the shorter and longer strings
        char currentChar;
        int basePosition = 0;
        int currentPosition = 0;
        boolean success = false;
        double myFitness = 0.0;
        
        //for each selected letter, test it against the longer string from the
        //current position to the last.
        for (int i = 0; i < solution.length; i++) 
        {
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
                basePosition += currentPosition - 1;
            }
            else{
                //decrease the fitness
                myFitness -= .5;
            }
        }
        
        return myFitness;
    }
    
    private int[] xoverSoution(int[] firstSolution, int[] secondSolution) {
        //Takes two possible solution int arrays and picks a random point
        //to crossover the values and returns the resulting array.
        //Could potentially have it repeat a set number of times depending on probability.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int[] mutateSolution(int[] parent) {
        //For a given solution, pick a random point and flip it.
        //Could repeat this a random number of times, repeating the 
        //probability check every time.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void breedParents(int populationSize, double pL, double pM, double pR) 
    {
        //get fitness of all parents
        for (int i = 0; i < populationSize; i++) {
            //for each solution, check its fitness
            fitness[i] = checkFitness(parents[i]);
        }
        
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
                    children[j]=xoverSoution(parents[firstParent], parents[secondParent]);
                    break;
                case 2:
                    //mutation - mutate the parent values
                    children[j]=mutateSolution(parents[firstParent]);
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
    
    /**
     * The following roulette wheel code is adapted from
     * the Wikipedia example code at the following link:
     * https://en.wikipedia.org/wiki/Fitness_proportionate_selection
     */
    // Returns the selected index based on the weights(probabilities)
    int rouletteSelect(double[] weight) {
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
    double randUniformPositive() {
            // easiest implementation
            return new Random().nextDouble();
    }
}
