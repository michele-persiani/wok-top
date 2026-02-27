#!/bin/bash

if [ ! -d "production_builds" ]; then
	mkdir -p production_builds/latest
	mkdir -p production_builds/old
fi

# echo "Insert app language for deploy: "
# read language

# prodInterfaceLanguage="interfaceLanguage=$language"
prodDbPath="dbPath=/home/rehab/MS-rehab/MS-rehab-vicenza.db"
# prodDbPath="dbPath=/home/rehab/MS-rehab/MS-rehab-test-politano.db"
prodPersistencePath='<property name="javax.persistence.jdbc.url" value="jdbc:sqlite:/home/rehab/MS-rehab/MS-rehab-vicenza.db"/>'
# prodPersistencePath='<property name="javax.persistence.jdbc.url" value="jdbc:sqlite:/home/rehab/MS-rehab/MS-rehab-test-politano.db"/>'

# localInterfaceLanguage=$(cat src/main/resources/META-INF/msrehab.properties | grep interfaceLanguage)
localDbPath=$(cat src/main/resources/META-INF/msrehab.properties | grep dbPath)
localPersistencePath=$(cat src/main/resources/META-INF/persistence.xml | grep url)

echo $localDbPath
echo $localPersistencePath

echo $prodDbPath
echo $prodPersistencePath

# echo "Setting language"
# sed -i "s|$localInterfaceLanguage|$prodInterfaceLanguage|g" src/main/resources/META-INF/msrehab.properties
echo "Setting DB path"
sed -i "s|$localDbPath|$prodDbPath|g" src/main/resources/META-INF/msrehab.properties
echo "Setting DB path in 'persistence.xml'"
sed -i "s|$localPersistencePath|$prodPersistencePath|g" src/main/resources/META-INF/persistence.xml

cp -r target target.bak

echo "Insert the name of this instance: "
read war_name

mvn clean package

last_war_name=$(ls production_builds/latest/ | grep .war | cut -d "." -f 1)

for build in $(ls production_builds/latest); do
	build_name=$(echo $build | cut -d . -f 1)
	if [ $war_name = $build_name ]; then
		echo "There is and old build build with the same name - moving it to 'old'"
		last_date=$(stat -c '%w' production_builds/latest/$build_name.war | cut -d "." -f 1 | sed 's/ /_/')
		mv production_builds/latest/$build production_builds/old/$build_name-$last_date.war
	fi
done
cp target/MS-rehab-pianificazione.war production_builds/latest/$war_name.war
# mv production_builds/latest/MS-rehab-nuova.war production_builds/$war_name.war
# scp $war_name.war swift:/home/lepianef
# ssh swift "sudo mv /home/lepianef/$war_name.war /opt/tomcat/webapps/"

rm -rf target
mv target.bak target

# sed -i "s|$prodInterfaceLanguage|$localInterfaceLanguage|g" src/main/resources/META-INF/msrehab.properties
sed -i "s|$prodDbPath|$localDbPath|g" src/main/resources/META-INF/msrehab.properties
sed -i "s|$prodPersistencePath|$localPersistencePath|g" src/main/resources/META-INF/persistence.xml
