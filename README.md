# Hebrew Short Message Stylometry
The goal for this project is to research and present a method for hebrew short messages stylometry.
The system that should be implemented should be able to identify an author of a message based on previously learned authoring style.
The goal is to study the features required for such identification, when working on short messages in Hebrew language.

the code of this project is arrange according to the architecture shown in https://github.com/asilev/NLP_FinalProject/blob/master/doc/architecture/NLP%20Author%20Classification%20Architecture.png.

The files in TwitterReader are used to retrieve data sets from Twitter, using the twitter4j framework.
This folder is meant to be compiled and run as a standalone executable with its own main method (Twitter4j is provided in a pre-compiled JAR library, twitter4j-core-4.0.4.jar, imported into the project).
Running it requires one parameter, which is the output path (where to put the tweets from tweeter, in two formats - rawa and normalized).
After reading the raw data, our code "normalizes" (or re-fromats) the data received in order to prepare it for processing by the YAP library.
In this process, two sets of files are generated - for each user, the raw data file named "ID_username.txt" (ID is a serial number starting with 00) and a normalized file named the same, with the suffix "-normalized".

The files with the "-normalized" suffix are then fed, one by one, manually, to the YAP tool. YAP (and GO) were downloaded from GitHub (and https://golang.org/), respectively.
After built into an executable tool, we used YAP with two commands:
"yap hebma -raw input.raw -out lattices.conll": This command runs YAP as a morphological analyzer. The ouptut file "lattices.conll" is then fed to the second command - 
"yap md -in lattices.conll -om output.conll": This command runs YAP disambiguation function and outputs the file "output.conll" which provides, for each tweet, its morphological analysis, allowing us to continue with the simulations.
 
** TODO: Also, resources should be moved the a dedicated folder for resouces (YAP outputs, twetter outputs, etc.).**

The files in Feature model are helper classes that help arrange the output data from YAP according to different types of feature sets:
**AllFeaturesModel** creates a vector with all the features from other models.
**BagOfWordsModel** has several static methods that create different types of vectors, where each feature in the vector is a word or word-based feature (bi-gram, word with POS, stemmed word, etc.).
**FeatureMarker** has a static method that creates a vector with style marker features.
**FeatureSelector** has a static method that creates a vector with selected features from other classes. This can be used to create several bag of words models combined, a style marker model with any sub-set of style features, or a combination of models.

These classes and static methods can be read from a learning model.

**LogisticRegression** uses Logistic regression to train a model by externalizing the model's parameters as files.
It then evaluates the model using the test corpus, and prints the accuracy.
This class is meant to be run as a stand-alone executable. It receives two parameters: The input path (that should contain outputs from YAP) and encoding type for these files(e.g. UTF8).
This class was not used intensively, some parameters are hard coded and can easily be changed in source code.
The features to be tested are put inside a model object by commenting and uncommenting any feature to be removed / added respectively.
The number of authors to train and evaluate is a variable named numOfAuthors.

**SupportVectorMachine** is used in order to prepare the inputs for the svmlib tool. This is also meant to be run as a stand-alone executable that receives parameters in a key-value format.
The keys and their default values are described hereafter, and where no default is mentioned it means that this is not an optional key:
* [-p input_path] 
* [-e encoding=UTF8] 
* [-t training_filename=training] 
* [-v evalutation_filename=evaluation] 
* [-f feature_mapper_filename=featureMapper] 
* [-m model_filename] 
* [-n number_of_authors=10]

The model_filename is a simple text file, that contains names of features that this model should be trained on.
Each line contains a name of a feature-set, and lines preceded with "%" are considered comments and their feature is not used by the model. The class creates the required input files for the svmlib tool, that should be run also as a stand-alone utility in order to evaluate the model.
We used the windows executable version of svmlib, in the following manner:
* Updating the model file according to the required feature sets.
* Running the SupportVectorMachine utility to produce inputs.
* If scaling is tested, running svm-scale.exe -l 0 <name of training input file> > <name of desired scaled training input file>
* Then, if scaling is tested, running svm-scale.exe -l 0 <name of evaluation input file> > <name of desired scaled evaluation input file>
* running svm-train.exe -t 1 -d 1 -r 0 -g <gamma parameter> -c <c parameter> <name of training / scaled training input file>
** The c and gamma parameters were changed in runs in order to find optimal values.
* running svm-predict.txt <name of evaluation / scaled evaluation input file> <name of training / scaked training model file> eval
* The output of the svm-predict was saved as the accuracy of the model.
Examples of python scripts that we used for this process can be found in resources folder.
