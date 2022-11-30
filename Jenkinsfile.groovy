pipeline {
    agent none

    stages {
        stage("build and deploy on Windows and Linux") {
            parallel {
                stage("windows") {
                    agent {
                        kubernetes {
                            yamlFile 'pods/windows.yaml'
                            defaultContainer 'shell'
                        }
                    }
                    stages {
                        stage("build") {
                            steps {
                                echo "build WINDOWS"
                               // run powershell command here
                               powershell 'Get-ChildItem Env: | Sort Name'
                               powershell 'Get-Date; Start-Sleep -Seconds 50; Get-Date'
                               powershell 'Write-Output "Hello WINDOWS"'

                            }
                        }
                        stage("deploy") {
                            steps {
                                echo "deploy threadA"
                            }
                        }
                    }
                }

                stage("linux") {
                    agent {
                        kubernetes {
                            yamlFile 'pods/linux.yaml'
                            defaultContainer 'maven'
                        }
                    }
                    stages {
                        stage("build") {
                            steps {
                                echo "build LINUX"
                                sh "hostname"
                                sh "env | sort"
                                sh "while :; do echo 'Hit CTRL+C'; sleep 1; done"
                                //sh 'sleep 100000'
                            }
                        }
                        stage("deploy") {
                            steps {
                                sh "echo deploy threadB"
                            }
                        }
                    }
                }
            }
        }
    }
}
