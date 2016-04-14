import numpy as np
import matplotlib.pyplot as plt

N = 5

# UC3
avg_mysql = (229345,261247,278472,418496,330346)
avg_neo4j = (26274,27530,26449,31701,35862)
avg_stardog = (39783,39451,39387,40292,41435)
avg_virtuoso = (26522,25872,25825,25405,28670)

ind = np.arange(N)	# the x locations for the groups
width = 0.15		# the width of the bars
opacity = 0.4

fig, ax = plt.subplots()

rects1 = ax.bar(ind, avg_mysql, width,
                 alpha=opacity,
                 color='b',
                 label='mysql')

rects2 = ax.bar(ind + width, avg_neo4j, width,
                 alpha=opacity,
                 color='r',
                 label='neo4j')

rects3 = ax.bar(ind + (width*2), avg_stardog, width,
                 alpha=opacity,
                 color='g',
                 label='stardog')

rects4 = ax.bar(ind + (width*3), avg_virtuoso, width,
                 alpha=opacity,
                 color='y',
                 label='virtuoso')

plt.xlabel('Distributions')
plt.ylabel('ms')
plt.xticks(ind + width, ('50/50', '60/40', '70/30', '80/20', '90/10'))
plt.legend()

plt.tight_layout()
plt.show()

