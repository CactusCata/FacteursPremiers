package fr.adamchareyre;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class Main {

	private static List<Long> listDeNombresPremiers = new ArrayList<>(); // Cette liste va contenir tout les nombres que l'ia recense
	private static final List<Long> value = new ArrayList<>(); //Ici la liste des nombres premiers du nombre choisie
	private static long basicNumber; //Cette variable va servir de "cache" si l'ia apprend un nouveau nombre

	public static final void main(final String[] args) {

		final Scanner scanner = new Scanner(System.in);
		
		//La m�thode va init la liste des nombres premiers connus par l'ia
		setup();

		System.out.println("Bienvenue � toi dans le choix des nombres premiers !");

		do {

			System.out.println("Veillez s�l�ctionner un nombre !");
			long number = 0;
			try {
				number = scanner.nextLong();
			} catch (Exception e) {
				System.out.println("Vous avez saisie un nombre invalide ou qui n'en n'est pas un !");
				scanner.close();
				return;
			}
			basicNumber = number;
			range();
			
			learnHimself(100000, 1000000);
			
//			recursivity(number);
//
//			System.out.printf("Pour la valeur %d, vous avez " + value + "\nVoulez-vous recommencer le jeu ?\n", number);
//			value.clear();
//			scanner.nextLine();
		} while (scanner.nextLine().equalsIgnoreCase("oui"));

		System.out.print("Merci d'avoir essay� ce programme !\nBonne journ�e !");
		scanner.close();

	}

	private static final long recursivity(final long number) {

		// Ici le billet de sortie de notre boucle r�cursive
		if (listDeNombresPremiers.contains(number)) {
			value.add(number);
			return number;
		}

		long k = 0;

		for (final Long nombrePremier : listDeNombresPremiers) {
			if (number % nombrePremier == 0) {
				value.add(nombrePremier);
				k = nombrePremier;
				break;
			}
		}

		if (k == 0) {
			registerNewNumber(number);
			System.out.println("L'IA a appris un nouveau nombre premier : " + number + " !");
			return recursivity(basicNumber);
		}

		return recursivity(number / k);

	}

	private static final void setup() {

		try {

			final File file = new File("src/res/recordednumbers.txt");

			//Si le fichier n'existe pas, alors on va le cr�er et lui mettre un minimum de connaissance de base
			if (!file.exists()) {
				System.out.println("Cr�ation du fichier qui permettra d'enregistrer les nombres premiers...");
				file.createNewFile();
				registerNewNumber(2);
				registerNewNumber(3);
				registerNewNumber(5);
				registerNewNumber(7);
				registerNewNumber(11);
				registerNewNumber(13);
			}

			final BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			//Ici nous allons rediriger chacun des nombres trouv�s dans le fichier dans la liste des nombres premiers connus
			while ((line = reader.readLine()) != null)
				listDeNombresPremiers.add(Long.parseLong(line));
			reader.close();

		} catch (final IOException e) {
			System.out.println("Erreur lors de la lecture du fichier :");
			e.printStackTrace();
		}

	}

	private static final void registerNewNumber(final long number) {
		try {
			//Ici c'est pour ajouter de nouveaux nombres dans le fichier de l'ia
			final BufferedWriter bufferedWriter = new BufferedWriter(
					new FileWriter("src/res/recordednumbers.txt", true));
			bufferedWriter.append(number + "\n");
			bufferedWriter.close();
		} catch (final IOException e) {
			System.out.println("Erreur lors de l'enregistrement :");
			e.printStackTrace();
		}

		//On ajoute le nombre dans la liste sinon la r�cursivit� va continuer
		listDeNombresPremiers.add(number);

	}

	private static final void range() {
		
		//Ayant reemarqu� des potentiels d�faillances j'ai d�cid� de cr�er cette
		// m�thode qui me permet de ranger la liste des nombres premiers dabs
		// l'ordre croissant
		
		final List<Long> newList = new ArrayList<Long>();
		
		//ici nous devons init l'array sinon on aura des probl�mes de tableau dans cette derniere
		for (int i = 0, j = listDeNombresPremiers.size(); i < j; i++)
			newList.add(0L);
		
		for (int i = 0, j = listDeNombresPremiers.size(); i < j; i++) {
			int value = 0;

			for (final Long longNumber : listDeNombresPremiers)
				if (listDeNombresPremiers.get(i) > longNumber) value++;

			newList.set(value, listDeNombresPremiers.get(i));

		}

		listDeNombresPremiers = newList;

	}
	
	private static final void learnHimself(long minValue, final long maxValue){
		for(; minValue <= maxValue; minValue++)
			recursivity(minValue);
	}
}
