import os, shutil

# if "AutoBan" in os.listdir():
#     shutil.rmtree("AutoBan")
# os.mkdir("AutoBan")

# shutil.copy("auto-ban-1.0-SNAPSHOT-All.jar", "AutoBan/")
# shutil.copy(os.getcwd() + "/../src/config.yml", "AutoBan/")
# if "AutoBan" in os.listdir("E:/Code Stuff/mc server/plugins/"):
#     shutil.rmtree("E:/Code Stuff/mc server/plugins/AutoBan")

# shutil.copytree("AutoBan/", "E:/Code Stuff/mc server/plugins/AutoBan")

if "AutoBan" in os.listdir("E:/Code Stuff/mc server/plugins/"):
    shutil.rmtree("E:/Code Stuff/mc server/plugins/AutoBan")

shutil.copyfile("auto-ban-1.0-SNAPSHOT-All.jar", "E:/Code Stuff/mc server/plugins/AutoBan.jar")
shutil.copyfile(os.getcwd() + "/../src/AutoBanConfig.yml", "E:/Code Stuff/mc server/plugins/AutoBanConfig.yml")