/*Pesara Amarasekera - Made from a Neural network tutorial to see it's inner workings*/

#include <vector>
#include <iostream>
#include <cstdlib>
#include <cassert>
#include <cmath>
using namespace std;


/***Connection struct***/
struct Connection
{
	double weight;
	double deltaWeight;
};


/*** Neuron Class ***/
class Neuron;

typedef vector<Neuron> Layer;

class Neuron
{
public:
	Neuron(unsigned numOutputs, unsigned myIndex);
	void setOutputVal(double inputVal){m_outputVal = inputVal;}
	double getOutputVal(void) const {return m_outputVal;}
	void feedForward(const Layer &prevLayer);
	void calcOutputGradients(double targetVal);
	void calcHiddenGradients(const Layer &nextLayer);
	void updateWeights(Layer &prevLayer);

private:
	static double eta; // [0..1] overall net training rate
	static double alpha; // [0..n] multiplier of last weight change (momentum)
	static double transferFunction(double sum);
	static double transferFunctionDerivative(double sum);
	static double randomWeight(void) {return rand()/ double(RAND_MAX);}
	double sumDOW(const Layer &nextLayer)const;
	unsigned m_myIndex;
	double m_outputVal;
	double m_gradient;
	vector<Connection> m_outputWeights;
};

double Neuron::eta = 0.15; //overall net learning rate
double Neuron::alpha = 0.5; //momentum multiplier of last deltaWeight


void Neuron::updateWeights(Layer &prevLayer)
{
	for(unsigned n=0;n<prevLayer.size();++n){
		Neuron &neuron = prevLayer[n];
		double oldDeltaWeight = neuron.m_outputWeights[m_myIndex].deltaWeight;
		double newDeltaWeight = 
				//Individual input, manified by the gradient and train rate:
				eta
				*neuron.getOutputVal()
				*m_gradient
				//Also add momentum = a fraction of the previous delta weight
				+alpha
				*oldDeltaWeight;
		neuron.m_outputWeights[m_myIndex].deltaWeight = newDeltaWeight;
		neuron.m_outputWeights[m_myIndex].weight += newDeltaWeight;
	}
}

double Neuron::sumDOW(const Layer &nextLayer)const
{
	double sum = 0.0;

	for(unsigned n=0; n<nextLayer.size()-1;++n){
		sum += m_outputWeights[n].weight*nextLayer[n].m_gradient;
	}

	return sum;
}

void Neuron::calcHiddenGradients(const Layer &nextLayer)
{
	double dow = sumDOW(nextLayer);
	m_gradient - dow*Neuron::transferFunctionDerivative(m_outputVal);
}

void Neuron::calcOutputGradients(double targetVals)
{
	double delta = targetVals - m_outputVal;
	m_gradient = delta*Neuron::transferFunctionDerivative(m_outputVal);
}

double Neuron::transferFunction(double sum)
{
	return tanh(sum);
}

double Neuron::transferFunctionDerivative(double sum)
{
	return 1.0-tanh(sum)*tanh(sum);
}

void Neuron::feedForward(const Layer &prevLayer)
{
	double sum = 0.0;

	for (unsigned n = 0; n < prevLayer.size(); ++n){
		sum += prevLayer[n].m_outputVal*
				prevLayer[n].m_outputWeights[m_myIndex].weight;
	}

	m_outputVal = Neuron::transferFunction(sum);
}

Neuron::Neuron(unsigned numOutputs, unsigned myIndex)
{
	for (unsigned c = 0; c < numOutputs; ++c){
		m_outputWeights.push_back(Connection());
		m_outputWeights.back.weight = randomWeight();
	}

	m_myIndex = myIndex;
}


/*** Net Class ***/
class Net
{
public:
	Net(const vector<unsigned> &topology);
	void feedForward(const vector<double> &inputVals);
	void backProp(const vector<double> &targetVals);
	void getResults(vector<double> &resultVals) const;

private:
	/* vector of containing layers containing vectors of neurons*/
	vector<Layer> m_layers;
	double m_error;
	double m_recentAverageError;
	double m_recentAverageSmoothingFactor;
};

void Net::getResults(vector<double> &resultVals) const
{
	resultVals.clear();
	for (unsigned n = 0; n < m_layers.back().size(); ++n){
		resultVals.push_back(m_layers.back()[n].getOutputVal());
	}
}

void Net::backProp(const vector<double> &targetVals) 
{
	//error calculation
	Layer &outputLayer = m_layers.back();
	m_error = 0.0;
	for (unsigned n = 0; n < outputLayer.size(); ++n){
		double delta = targetVals[n]-outputLayer[n].getOutputVal();
		m_error += delta*delta;
	}
	m_error /= outputLayer.size()-1;
	m_error = sqrt(m_error);
	//recent average measurement
	m_recentAverageError = (m_recentAverageError*m_recentAverageSmoothingFactor+m_error)/
							(m_recentAverageSmoothingFactor+1.0);	
	//output gradient calculation
	for(unsigned n = 0; n < outputLayer.size(); ++n){
		outputLayer[n].calcOutputGradients(targetVals[n]);
	}				
	//hidden gradient calculation
	for(unsigned layerNum=m_layers.size()-2; layerNum>0;--layerNum){
		Layer &hiddenLayer = m_layers[layerNum];
		Layer &nextLayer = m_layers[layerNum+1];

		for (unsigned n = 0; n < hiddenLayer.size(); ++n){
			hiddenLayer[n].calcHiddenGradients(nextLayer);
		}
	}
	//weight update
	for(unsigned layerNum=m_layers.size()-1; layerNum>0;--layerNum){
		Layer &layer = m_layers[layerNum];
		Layer &prevLayer = m_layers[layerNum-1];

		for(unsigned n = 0; n < layer.size(); ++n){
			layer[n].updateWeights(prevLayer);
		}
	}
}

void Net::feedForward(const vector<double> &inputVals)
{
	assert (inputVals.size() == m_layers[0].size()-1);
	/*set input values*/
	for (unsigned i = 0; i < inputVals.size(); ++i){
		m_layers[0][i].setOutputVal(inputVals[i]);
	}

	/*forward propagation*/
	for (unsigned layerNum= 1; layerNum < m_layers.size(); ++layerNum){
		Layer &prevLayer = m_layers[layerNum-1];
		for (unsigned n= 0; n < m_layers[layerNum].size()-1; ++n){
			m_layers[layerNum][n].feedForward(prevLayer); 
		}
	}
}


Net::Net(const vector<unsigned> &topology)
{
	unsigned numLayers = topology.size();
	unsigned numOutputs;
	for (unsigned layerNum = 0; layerNum < numLayers; ++layerNum){
		m_layers.push_back(Layer());
		numOutputs =  layerNum == topology.size()-1 ? 0 : topology[layerNum+1];
		//its actually going to be topology[layernum] neruons + 1 more for bias
		for (unsigned neuroNum = 0; neuroNum <= topology[layerNum]; ++neuroNum){
			m_layers.back().push_back(Neuron(numOutputs, neuroNum)); 
			cout << "Made a Neuron"<< endl;
		}
		//force bias
		m_layers.back().back().setOutputVal(1.0);
	}
}


int main()
{
	vector<unsigned> topology;//contains size of layers
	topology.push_back(3);
	topology.push_back(6);
	topology.push_back(9);
	topology.push_back(6);
	topology.push_back(3);
	topology.push_back(1);
	Net myNet(topology);

	vector<double> inputVals;
	myNet.feedForward(inputVals);
	
	vector<double> targetVals;
	myNet.backProp(targetVals);
	
	vector<double> resultVals;
	myNet.getResults(resultVals);

	//create a training data set and algorithm for training
}