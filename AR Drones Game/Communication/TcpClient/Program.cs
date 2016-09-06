using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net;
using System.Net.Sockets;

namespace UnityTcpClient
{

    //  A Cool tcp client for the Battleship project :-)
    //  By Eli Hundia
    class Program
    {
        private string ipString;
        private int connectionPort;
        TcpClient tcpclnt;
        NetworkStream stm;
        ASCIIEncoding asen;
        public void init(string ip, int port)
        {
            ipString = ip;
            connectionPort = port;
            asen = new ASCIIEncoding();
        }

        public void connect()
        {
            //  Create a TCP Client
            tcpclnt = new TcpClient();
            try
            {
                //  Connect to the server
                Console.WriteLine("Connecting.....");
                tcpclnt.Connect(ipString, connectionPort);
                Console.WriteLine("Connected");
                stm = tcpclnt.GetStream();
            }
            catch (Exception e)
            {
                Console.WriteLine("Error connecting to server: " + e.StackTrace);
            }
        }

        public void sendMsg(string msgToSend)
        {
            try
            {
                byte[] ba = asen.GetBytes(msgToSend);
                stm.Write(ba, 0, ba.Length);
            }

            catch (Exception e)
            {
                Console.WriteLine("Error Writing to server: " + e.StackTrace);
            }
        }

        public void closeConnection()
        {
            try {
                tcpclnt.Close();
            }
            
            catch (Exception e)
            {
                Console.WriteLine("Error Closing the socket: " + e.StackTrace);
            }
        }
        static void Main(string[] args)
        {
            Program client = new Program();
            client.init("127.0.0.1", 8001);
            client.connect();
            for (int i = 0; i < 10; i++)
            {
                Console.Write("Enter the string to be transmitted : ");
                String str = Console.ReadLine();
                client.sendMsg(str);
            }

            client.closeConnection();
            
        }
    }
}
