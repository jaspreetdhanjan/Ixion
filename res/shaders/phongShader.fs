#version 330 core

out vec4 fragColour;

in vec4 vertexColour;
in vec3 FragPos;  
in vec3 Normal; 

uniform vec3 lightPos;
uniform vec3 lightColour;
uniform vec3 viewPos;

void main() {
    // Ambient
    float ambientStrength = 0.5f;
    vec3 ambient = ambientStrength * lightColour;
  	
    // Diffuse 
    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(lightPos - FragPos);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * lightColour;
    
    // Specular
    float specularStrength = 0.5f;
    vec3 viewDir = normalize(viewPos - FragPos);
    vec3 reflectDir = reflect(-lightDir, norm);  
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32);
    vec3 specular = specularStrength * spec * lightColour;  
        
    vec3 result = ambient + diffuse + specular;
//    vec3 red = vec4(1.0, 0.0, 0.0);    
	fragColour = vec4(result, 1.0);
}