import os
import subprocess

gammas = (0.00003, 0.0001, 0.0005, 0.002, 0.008, 0.03125, 0.125, 0.5, 2, 8, 32, 128)
cs = (0.03125, 0.125, 0.5, 2, 8, 32, 128, 512, 2048, 8192, 32768)

# svmprepare usage:
# java -jar svmprepare.jar
#	 * [-p input_path]
#	 * [-e encoding=UTF8]
#	 * [-t training_filename=training]
#	 * [-v evalutation_filename=evaluation]
#	 * [-f feature_mapper_filename=featureMapper]
#	 * [-m model_filename]
#	 * [-n number_of_authors=10]

os.system("java -jar svmprepare.jar -t training.svm.bosw -v evaluation.svm.bosw -m model.txt")

# scaling usage:
# svm-scale.exe -l 0 input_file_name > output_file_name
# os.system("svm-scale.exe -l 0 training > training.scaled")
# print("svm-scale.exe -l 0 evaluation > evaluation.scaled")

# grid = [[0]*len(cs)]*len(gammas)

if os.path.exists("resultFile.csv"):
    os.remove("resultFile.csv")
f = open("resultFile.csv", "w+")
f.write(", ")
for c in cs:
    f.write(str(c) + ",")
f.write("\n")
for g in gammas:
    f.write(str(g) + ",")
    for c in cs:
        print ("g="+str(g)+", c="+str(c))
        os.system("svm-train.exe -t 1 -d 1 -r 0 -g "+str(g)+" -c "+str(c) + " training.svm.bosw")
        output = subprocess.Popen(["svm-predict.exe", "evaluation.svm.bosw", "training.svm.bosw.model", "eval"],
                                  stdout=subprocess.PIPE).communicate()[0]
        splitOutput = output.split(" ")
        f.write(splitOutput[2]+",")
        f.flush()
    f.write("\n")
f.close()
