package com.example.calculimc

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextPoids = findViewById<EditText>(R.id.editTextPoids)
        val editTextTaille = findViewById<EditText>(R.id.editTextTaille)
        val buttonCalculer = findViewById<Button>(R.id.buttonCalculer)
        val buttonEffacer = findViewById<Button>(R.id.buttonEffacer)
        val textViewImc = findViewById<TextView>(R.id.textViewImc)
        val textViewCategorie = findViewById<TextView>(R.id.textViewCategorie)

        buttonCalculer.setOnClickListener {
            // 1. Récupérer les saisies
            val poidsTexte = editTextPoids.text.toString().trim()
            val tailleTexte = editTextTaille.text.toString().trim()

            // 2. Vérifier que les champs sont renseignés
            if (poidsTexte.isEmpty() || tailleTexte.isEmpty()) {
                if (poidsTexte.isEmpty()) {
                    editTextPoids.error = getString(R.string.erreur_champ_vide)
                }
                if (tailleTexte.isEmpty()) {
                    editTextTaille.error = getString(R.string.erreur_champ_vide)
                }
                Toast.makeText(this, R.string.toast_completer, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 3. Convertir en décimaux (accepte la virgule ou le point)
            val poids = poidsTexte.replace(',', '.').toDoubleOrNull()
            val taille = tailleTexte.replace(',', '.').toDoubleOrNull()

            if (poids == null) {
                editTextPoids.error = getString(R.string.erreur_valeur_invalide)
            }
            if (taille == null) {
                editTextTaille.error = getString(R.string.erreur_valeur_invalide)
            }
            if (poids == null || taille == null) {
                return@setOnClickListener
            }

            // 4. Vérifier que les valeurs sont strictement positives
            if (poids <= 0 || taille <= 0) {
                if (poids <= 0) {
                    editTextPoids.error = getString(R.string.erreur_valeur_positive)
                }
                if (taille <= 0) {
                    editTextTaille.error = getString(R.string.erreur_valeur_positive)
                }
                Toast.makeText(this, R.string.toast_corriger, Toast.LENGTH_SHORT).show()
                textViewImc.text = ""
                textViewCategorie.text = ""
                return@setOnClickListener
            }

            // 5. Calcul de l'IMC
            val imc = poids / (taille * taille)

            // 6. Arrondi à 2 décimales (via le format %.2f dans strings.xml)
            textViewImc.text = getString(R.string.resultat_imc, imc)

            // 7. Catégorie avec if / else if / else
            val categorie: String
            val couleur: Int
            if (imc < 18.5) {
                categorie = getString(R.string.cat_insuffisance)
                couleur = Color.parseColor("#FF9800") // orange
            } else if (imc < 25) {
                categorie = getString(R.string.cat_normale)
                couleur = Color.parseColor("#2E7D32") // vert
            } else if (imc < 30) {
                categorie = getString(R.string.cat_surpoids)
                couleur = Color.parseColor("#FF9800") // orange
            } else if (imc < 35) {
                categorie = getString(R.string.cat_obesite_moderee)
                couleur = Color.parseColor("#D32F2F") // rouge
            } else if (imc < 40) {
                categorie = getString(R.string.cat_obesite_severe)
                couleur = Color.parseColor("#D32F2F") // rouge
            } else {
                categorie = getString(R.string.cat_obesite_morbide)
                couleur = Color.parseColor("#8B0000") // rouge foncé
            }

            // 8. Affichage
            textViewCategorie.text = categorie
            textViewCategorie.setTextColor(couleur)
            textViewImc.setTextColor(couleur)
        }

        // Bouton Effacer
        buttonEffacer.setOnClickListener {
            editTextPoids.text.clear()
            editTextTaille.text.clear()
            editTextPoids.error = null
            editTextTaille.error = null
            textViewImc.text = ""
            textViewCategorie.text = ""
            editTextPoids.requestFocus()
        }
    }
}