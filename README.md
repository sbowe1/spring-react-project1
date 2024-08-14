# Learning Platform

Learning Platform is a web application that allows its users to create their own customized learning plans.

## Contributors

[Sidney Bowe](https://linkedin.com/in/sidney-bowe) - *back end lead*

[Peyton Kellogg](https://www.linkedin.com/in/peyton-kellogg/) - *front end lead; project management*

[Robin Menzi](https://www.linkedin.com/in/robin-menzi/) - *UX design lead*

[Savannah Hitney](https://www.linkedin.com/in/savannahhitney/) - *fullstack developer*

[Lauren DuCharme](https://www.linkedin.com/in/lauren-ducharme-32971185/) - *consulting sr. Java engineer*

## Contributing

Clone and pull the project down to your local machine.

```
git clone git@github.com:sbowe1/spring-react-project1.git
```

### Back end

Install [JDK 17](https://www.oracle.com/java/technologies/downloads/#java17) for your operating system.

To ensure your code is formatted for the project, create local file, ~/.m2/settings.xml.

```
touch ~/.m2/settings.xml
```

Within `~/.m2/settings.xml`, add the below snippet:

```
<pluginGroups>
	<pluginGroup>io.spring.javaformat</pluginGroup>
</pluginGroups>
```

From back end directory, run the command below before commits:

```
cd p1-backend
./mvnw spring-javaformat:apply
```

Start the back end application using hot keys for your IDE.

### Front end

From the front end directory, install the front end dependencies to your local machine.

```
cd p1-frontend
npm i
```

Install Next.js’s recommended package manager, pnpm.

```
npm i -g pnpm
```

From the front end directory, start the front end web server and get developing.

```
pnpm dev
```
