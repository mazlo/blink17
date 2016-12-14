import numpy as np
import matplotlib.pyplot as plt

N = 5

# UC4
avg_mysql = (76751,76680,32158,51712,14732)
avg_neo4j = (25408,26414,29794,30524,55315)
avg_stardog = (38167,38750,39821,38908,38113)
avg_virtuoso = (25315,26003,25575,27705,27705)

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

