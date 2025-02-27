# Ouvre le fichier en lecture et le fichier de sortie en écriture
with open("cas-tests.txt", "r") as input_file, open("newtest-case.txt", "w") as output_file:
    for line in input_file:
        tokens = line.split()  # Séparer la ligne en tokens
        if len(tokens) == 12:  # Vérifier si la ligne a exactement 12 tokens
            output_file.write(line)  # Écrire la ligne valide dans le nouveau fichier

print("Filtrage terminé ! Les lignes valides sont enregistrées dans 'newtest-case.txt'.")
