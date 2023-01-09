import matplotlib.pyplot as plt
import sqlite3
import time
import os
from datetime import datetime

SECONDS_IN_HOUR = 3600
SECONDS_IN_DAY = 24*SECONDS_IN_HOUR

mx, mn = [], []


def get_temperatures_among_days(url):
    con = sqlite3.connect(url)
    cur = con.cursor()

    dt = datetime.today().strftime('%Y%m%d')
    max_result = cur.execute("SELECT temp FROM MaxTemp ORDER BY date")
    max_temperature = [i[0] for i in max_result.fetchall()]

    min_result = cur.execute("SELECT temp FROM MinTemp ORDER BY date")
    min_temperature = [i[0] for i in min_result.fetchall()]
    if (len(min_temperature) <= 20):
        return max_temperature, min_temperature
    else:
        return max_temperature[0:20], min_temperature[0:20]


def plot_temp(min_temperatures, max_temperatures, gp_url):
    x = [i for i in range(1, len(min_temperatures) + 1)]

    plt.figure(figsize=(10, 8))
    plt.scatter(x, max_temperatures, c='red', label='maximum')
    plt.scatter(x, min_temperatures, c='blue', label='minimum')
    plt.title('Temperature per day (last 20 d.)')
    plt.xlabel('Day')
    plt.ylabel('Hour')
    plt.legend(loc='center left')

    if not os.path.exists(gp_url + "/" + datetime.today().strftime('%h')):
        os.mkdir(gp_url + "/" + datetime.today().strftime('%h'))
    plt.savefig(gp_url + "/" + datetime.today().strftime('%h') + "/" + datetime.today().strftime('%Y%m%d'))


def temperatures(url):
    con = sqlite3.connect(url)
    cur = con.cursor()

    dt = datetime.today().strftime('%Y%m%d')
    max_result = cur.execute("SELECT temp FROM MaxTemp WHERE date = " + dt)
    max_temperature = max_result.fetchall()[0][0]

    min_result = cur.execute("SELECT temp FROM MinTemp WHERE date = " + dt)
    min_temperature = min_result.fetchall()[0][0]
    return max_temperature, min_temperature


def update_temp_lists(url):
    max_temperature, min_temperature = temperatures(url)
    mx.append(max_temperature)
    mn.append(min_temperature)


def temp_per_hour(db_url, gp_url):
    update_temp_lists(url_db)
    x = [i for i in range(1, len(mx) + 1)]

    plt.figure(figsize=(10, 8))
    plt.scatter(x, mx, c='red', label='maximum')
    plt.scatter(x, mn, c='blue', label='minimum')
    plt.title('Temperature per hour')
    plt.xlabel('Hour')
    plt.ylabel('Temperature')
    plt.legend(loc='center left')

    if not os.path.exists(gp_url + "/" + datetime.today().strftime('%h')):
        os.mkdir(gp_url + "/" + datetime.today().strftime('%h'))
    plt.savefig(gp_url + "/" + datetime.today().strftime('%h') + "/" + datetime.today().strftime('%Y%m%d_h%H'))


def temp_per_day(db_url, gp_url):
    con = sqlite3.connect(db_url)
    cur = con.cursor()

    x = [i for i in range(1, len(mx) + 1)]

    plt.figure(figsize=(10, 8))
    plt.scatter(x, mx, c='red', label='maximum')
    plt.scatter(x, mn, c='blue', label='minimum')
    plt.title('Temperature per day')
    plt.xlabel('Day')
    plt.ylabel('Temperature')
    plt.legend(loc='center left')

    if not os.path.exists(gp_url + "/" + datetime.today().strftime('%h')):
        os.mkdir(gp_url + "/" + datetime.today().strftime('%h'))
    plt.savefig(gp_url + "/" + datetime.today().strftime('%h') + "/" + datetime.today().strftime('%Y%m%d'))


def get_urls_by_terminal():
    url_db = input('Introduce your datamart url with the appropriate python format: ')
    url_gp = input('Introduce your storage space url with the appropriate python format: ')
    return url_db, url_gp


def my_urls():
    url_db = 'C:/Users/carde/Desktop/ULPGC/Aemet/temp.db'
    url_gp = 'C:/Users/carde/Desktop/ULPGC/Aemet/graphs'
    return url_db, url_gp


if __name__ == '__main__':
    url_db, url_gp = my_urls()
    date = datetime.today().strftime('%Y%m%d')
    hour = 0

    while True:
        max_temperature, min_temperature = get_temperatures_among_days(url_db)
        plot_temp(min_temperature, max_temperature, url_gp)
        time.sleep(SECONDS_IN_DAY)
