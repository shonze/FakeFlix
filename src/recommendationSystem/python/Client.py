import socket
import sys

class Client:
    def __init__(self, server_ip, port):
        self.server_ip = server_ip
        self.port = port
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        
        try:
            self.sock.connect((server_ip, port))
        except Exception as e:
            sys.exit(1)

    def run(self):
        try:
            while True:
                # Read command from the user
                command = input().strip()
                if not command:
                    command = "x"
                
                # Send the command to the server
                self.sock.sendall(command.encode())
                
                # Receive the server's response
                response = self.sock.recv(1024).decode()
                if not response:
                    break
                
                # Print the server's response
                print(f"{response}", end="")
        
        except KeyboardInterrupt:
            self.sock.close()
        except Exception as e:
            self.sock.close()
        finally:
            self.sock.close()

if __name__ == "__main__":
    if len(sys.argv) != 3:
        sys.exit(1)
    
    server_ip = sys.argv[1]
    port = int(sys.argv[2])
    client = Client(server_ip, port)
    client.run()
