from ultralytics import YOLO
import os

save_dir = '/Users/coutinhonobre/Projects/yolo/curso_yolov8/result'
os.makedirs(save_dir, exist_ok=True)

def main():
    modelo = YOLO('yolov8n.pt')
    resultado = modelo.train(data='/Users/coutinhonobre/Projects/yolo/curso_yolov8/dataset/data.yml', epochs=30, imgsz=640, workers=2, project=save_dir, device='mps')

if __name__ == '__main__':
    main()    